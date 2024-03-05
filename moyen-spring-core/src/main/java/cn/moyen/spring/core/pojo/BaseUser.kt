package cn.moyen.spring.core.pojo

import io.swagger.v3.oas.annotations.media.Schema

// Date: 2024-03-01 13:36

/**
 * 用户抽象类
 */
abstract class BaseUser() : BaseEntity(), IUser
{
    @Schema(description = "用户 uuid")
    override var uuid: String? = null

    @Schema(description = "用户名")
    override var username: String? = null

    @Schema(description = "用户密码")
    override var password: String? = null

    @Schema(description = "用于解析密码的盐")
    override var salt: String? = null

    @Schema(description = "最后一次登录的 IP")
    override var lastIp: String? = null
}