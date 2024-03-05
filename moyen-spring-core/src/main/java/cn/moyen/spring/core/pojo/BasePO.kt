package cn.moyen.spring.core.pojo

// Date: 2024-03-01 13:36

import io.swagger.v3.oas.annotations.media.Schema

/**
 * PO 抽象类
 */
abstract class BasePO : PO
{
    @Schema(description = "数据 ID")
    override var id: Long? = null

    @Schema(description = "创建时间的时间戳")
    override var createTime: Long? = null
}