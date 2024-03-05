package cn.moyen.spring.core.pojo

// Date: 2024-03-01 13:36

/**
 * 实体类接口
 */
interface Entity : PO
{
    /**
     * 是否启用
     */
    var enable: Byte?

    /**
     * 备注
     */
    var remark: String?

    /**
     * 是否已被删除
     */
    var deleted: Byte?

    /**
     * 最后修改时间的时间戳
     */
    var modifiedTime: Long?
}