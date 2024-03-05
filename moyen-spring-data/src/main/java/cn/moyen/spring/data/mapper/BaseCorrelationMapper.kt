package cn.moyen.spring.data.mapper

import cn.moyen.spring.core.pojo.Correlation
import org.apache.ibatis.annotations.Param

// Date: 2024-02-29 21:11

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