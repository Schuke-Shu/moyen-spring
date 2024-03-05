package cn.moyen.spring.core.pojo

// Date: 2024-03-01 13:36

/**
 * PO 类接口
 */
interface PO
{
    /**
     * 数据 ID
     */
    var id: Long?

    /**
     * 创建时间的时间戳
     */
    var createTime: Long?
}