package cn.moyen.spring.log

import cn.moyen.spring.core.pojo.BasePO
import cn.moyen.spring.log.enums.ServiceType
import cn.moyen.spring.log.enums.Status
import cn.moyen.spring.log.enums.UserType
import io.swagger.v3.oas.annotations.media.Schema

class SysLog : BasePO()
{
    @Schema(description = "业务状态")
    var status: Status? = null

    @Schema(description = "业务处理时间")
    var castTime: String? = null

    @Schema(description = "业务模块")
    var title: String? = null

    @Schema(description = "描述")
    var description: String? = null

    @Schema(description = "请求方法")
    var method: String? = null

    @Schema(description = "请求地址")
    var uri: String? = null

    @Schema(description = "请求 IP")
    var ip: String? = null

    @Schema(description = "请求参数")
    var params: String? = null

    @Schema(description = "响应结果")
    var result: String? = null

    @Schema(description = "操作人 id")
    var userId: Long? = null

    @Schema(description = "操作人类型")
    var userType: UserType? = null

    @Schema(description = "业务类型")
    var serviceType: ServiceType? = null

    @Schema(description = "异常编号")
    var errorCode: Int? = null
}
