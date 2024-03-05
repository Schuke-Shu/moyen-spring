package cn.moyen.spring.core.pojo

// Date: 2024-03-01 13:36

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