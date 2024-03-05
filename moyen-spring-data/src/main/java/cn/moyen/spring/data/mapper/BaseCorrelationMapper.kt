package cn.schuke.website.mapper

import cn.schuke.website.pojo.Correlation
import cn.schuke.website.pojo.PO
import cn.schuke.website.pojo.PageParams
import com.github.pagehelper.PageInfo
import org.apache.ibatis.annotations.Param

// Date: 2024-02-29 21:11

/**
 * Mapper 模板
 */
interface BaseMapper<E : PO>
{
    /**
     * 新增一条数据
     */
    fun save(e: E): Int

    /**
     * 批量插入数据
     */
    fun saves(list: Collection<E>): Int

    /**
     * 根据主键删除一条数据
     */
    fun remove(id: Long): Int

    /**
     * 根据主键数组批量删除数据
     */
    fun removes(list: Collection<Long>): Int

    /**
     * 条件删除
     */
    fun removeBy(e: E): Int

    /**
     * 清空数据
     */
    fun clear()

    /**
     * 编辑一条数据
     */
    fun edit(e: E): Int

    /**
     * 根据主键查询数据
     */
    fun query(id: Long): E?

    /**
     * 列出所有数据
     */
    fun list(): Collection<E>

    /**
     * 根据主键列表列出数据
     */
    fun lists(list: Collection<Long>): Collection<E>

    /**
     * 条件查询
     */
    fun listBy(e: E): Collection<E>

    /**
     * 分页查询
     */
    fun <P : PageParams> page(page: P): PageInfo<E>
    {
        TODO("page")
//        return pagination(page) { p -> this.list() }
    }

    /**
     * 统计表中数据总量
     */
    fun count(): Int

    /**
     * 根据条件统计表中数据量
     */
    fun countBy(e: E): Int
}

/**
 * 关联表 Mapper 模板
 */
interface BaseCorrelationMapper<T : Correlation> : BaseMapper<T>
{
    /**
     * 根据左表主键批量删除
     */
    fun removeByLid(lid: Long): Int

    /**
     * 根据右表主键批量删除
     */
    fun removeByRid(rid: Long): Int

    /**
     * 根据双方主键查询数据
     */
    fun queries(@Param("lid") lid: Long, @Param("rid") rid: Long): T

    /**
     * 根据左表主键列出关联表数据
     */
    fun listByLid(id: Long): Collection<T>

    /**
     * 根据右表主键列出关联表数据
     */
    fun listByRid(id: Long): Collection<T>
}