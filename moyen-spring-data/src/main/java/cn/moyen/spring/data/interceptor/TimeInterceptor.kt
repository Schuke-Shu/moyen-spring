package cn.moyen.spring.data.interceptor

import cn.moyen.spring.core.REQUEST_TIME
import cn.moyen.spring.core.exception.ConfigException
import cn.moyen.spring.core.util.getRequestAttribute
import org.apache.ibatis.executor.statement.StatementHandler
import org.apache.ibatis.mapping.BoundSql
import org.apache.ibatis.plugin.Interceptor
import org.apache.ibatis.plugin.Intercepts
import org.apache.ibatis.plugin.Invocation
import org.apache.ibatis.plugin.Signature
import org.slf4j.LoggerFactory
import java.sql.Connection

// 拦截器
// Date: 2024-02-28 12:06

@Intercepts(
    Signature(
        type = StatementHandler::class,
        method = "prepare",
        args = [Connection::class, Int::class]
    )
)
class TimeInterceptor : Interceptor
{
    private val log = LoggerFactory.getLogger(TimeInterceptor::class.java)

    private val sqlField: String = "sql"

    /**
     * 自动添加的创建时间字段
     */
    private val fieldCreate = "create_time"

    /**
     * 自动更新时间的字段
     */
    private val fieldModified = "modified_time"

    /**
     * 查找SQL类型的正则表达式：INSERT
     */
    private val sqlTypePatternInsert = Regex("^insert\\s", RegexOption.IGNORE_CASE)

    /**
     * 查找SQL类型的正则表达式：UPDATE
     */
    private val sqlTypePatternUpdate = Regex("^update\\s", RegexOption.IGNORE_CASE)

    /**
     * 查询SQL语句片段的正则表达式：create_time片段
     */
    private val sqlStatementPatternCreate = Regex(",\\s*$fieldCreate\\s*[,)]?", RegexOption.IGNORE_CASE)

    /**
     * 查询SQL语句片段的正则表达式：modified_time片段
     */
    private val sqlStatementPatternUpdate = Regex(",\\s*$fieldModified\\s*=", RegexOption.IGNORE_CASE)

    /**
     * 查询SQL语句片段的正则表达式：WHERE子句
     */
    private val sqlStatementPatternWhere = Regex("\\s+where\\s+", RegexOption.IGNORE_CASE)

    /**
     * 查询SQL语句片段的正则表达式：VALUES子句
     */
    private val sqlStatementPatternValue = Regex("\\)\\s*values?\\s*\\(", RegexOption.IGNORE_CASE)

    override fun intercept(invocation: Invocation): Any =
        invocation.run {
            log.run {
                // 获取 BoundSql
                debug("Intercepting sql...")
                val boundSql =
                    invocation.target.let {
                        if (it is StatementHandler) it.boundSql
                        else throw ConfigException("[StatementHandler] not found, check the interceptor configuration please")
                    }

                val sql: String = boundSql.sql.lowercase().replace(Regex("\\s+"), " ").trim()
                debug("old sql: {}", sql)

                // 生成新 sql
                val newSql: String? =
                    if (sqlTypePatternInsert.matches(sql)) makeCreateSql(sql)
                    else if (sqlTypePatternUpdate.matches(sql)) makeModifiedSql(sql)
                    else null

                if (newSql != null)
                    BoundSql::class.java
                        .getDeclaredField(sqlField)
                        .run {
                            setAccessible(true)
                            set(boundSql, newSql.also { debug("new sql: {}", newSql) })
                        }
                else
                    debug("The condition for interception are not met, passed")
            }

            // 放行
            proceed()
        }

    private fun makeCreateSql(sql: String): String? =
        log.run {
            debug("Type of old sql is [INSERT], insert create-time field")
            sqlStatementPatternCreate.find(sql)
                ?.let { debug("Old sql already contains the create-time field"); null }
                ?: StringBuilder(sql)
                    .run {
                        val newSql = this
                        sqlStatementPatternValue.find(newSql)
                            ?.run {
                                // 插入字段名称
                                val filedNames = ",$fieldCreate"
                                insert(range.first, filedNames)

                                // 查找参数值位置
                                val param = ",${getRequestAttribute(REQUEST_TIME)}"
                                Regex("\\)")
                                    .findAll(newSql, range.last + filedNames.length)
                                    .run {
                                        if (count() == 0)
                                            warn("Sql insertion location not found").let { null }
                                        else
                                            forEach { insert(it.range.first, param) }.let { newSql }
                                    }
                            } ?: warn("Fragment [ ) values ( ] not found").let { null }
                    }
                    ?.toString()
        }

    private fun makeModifiedSql(sql: String): String? =
        log.run {
            debug("Type of old sql is [UPDATE], insert modified-time field")
            sqlStatementPatternUpdate.find(sql)
                ?.let { debug("Old sql already contains the modified-time field"); null }
                ?: StringBuilder(sql)
                    .run {
                        sqlStatementPatternWhere.find(sql)
                            ?.run {
                                insert(range.first, ",$fieldModified='${getRequestAttribute(REQUEST_TIME)}'")
                                toString()
                            }
                    }
        }
}