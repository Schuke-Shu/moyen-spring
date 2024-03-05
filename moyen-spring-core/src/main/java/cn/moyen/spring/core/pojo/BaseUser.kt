package cn.moyen.spring.core

import cn.moyen.spring.core.pojo.BaseEntity
import cn.moyen.spring.core.pojo.BasePO
import cn.moyen.spring.core.pojo.Correlation
import cn.moyen.spring.core.pojo.Entity
import io.swagger.v3.oas.annotations.media.Schema

// Pojo 实体接口
// Date: 2024-03-01 13:36

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

/**
 * 用户实体接口
 */
interface IUser : Entity
{
    /**
     * 用户 uuid
     */
    var uuid: String?

    /**
     * 用户名
     */
    var username: String?

    /**
     * 用户密码
     */
    var password: String?

    /**
     * 用于解析密码的盐
     */
    var salt: String?

    /**
     * 最后一次登录的 IP
     */
    var lastIp: String?
}

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