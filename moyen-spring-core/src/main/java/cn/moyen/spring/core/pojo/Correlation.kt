package cn.moyen.spring.core.pojo

// Date: 2024-03-01 13:36

/**
 * 关联表接口
 */
interface Correlation : PO
{
    /**
     * 左表主键值
     */
    var lid: Long?

    /**
     * 右表主键值
     */
    var rid: Long?
}