package cn.moyen.spring.core.pojo

// Date: 2024-03-01 13:36

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 实体抽象类
 */
abstract class BaseEntity : BasePO(), Entity
{
    @Schema(description = "是否启用")
    override var enable: Byte? = null

    @Schema(description = "备注")
    override var remark: String? = null

    @Schema(description = "是否已被删除")
    override var deleted: Byte? = null

    @Schema(description = "最后修改时间的时间戳")
    override var modifiedTime: Long? = null
}