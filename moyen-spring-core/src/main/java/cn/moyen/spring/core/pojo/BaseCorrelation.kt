package cn.moyen.spring.core.pojo

// Date: 2024-03-01 13:36

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 关联表接口
 */
abstract class BaseCorrelation : BasePO(), Correlation
{
    @Schema(description = "左表主键值")
    override var lid: Long? = null

    @Schema(description = "右表主键值")
    override var rid: Long? = null
}