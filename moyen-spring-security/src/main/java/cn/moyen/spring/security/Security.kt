package cn.moyen.spring.security

import cn.moyen.spring.core.pojo.IUser
import cn.moyen.spring.core.service.IUserService
import cn.moyen.spring.security.entity.Principal
import io.jsonwebtoken.Claims
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.util.matcher.RequestMatcher

// Date: 2024-03-04 13:23

/**
 * Token 长度下限
 */
const val TOKEN_MIN_LENGTH = 105

/**
 * 注册地址
 */
const val REGISTER_URI = "/register"

/**
 * 登录地址
 */
const val LOGIN_URI = "/login"

/**
 * 存放 token 的请求头的名称
 */
const val AUTH_HEADER = "Authorization"

/**
 * Token 存储字段名：当事人 id
 */
const val TOKEN_KEY_ID = "id"

/**
 * Token 存储字段名：用户名
 */
const val TOKEN_KEY_USERNAME = "username"

/**
 * Token 存储字段名：用户手机号
 */
const val TOKEN_KEY_PHONE = "phone"

/**
 * Token 存储字段名：用户邮箱
 */
const val TOKEN_KEY_EMAIL = "email"

/**
 * Token 存储字段名：请求 IP 地址
 */
const val TOKEN_KEY_IP = "ip"

/**
 * Token 存储字段名：用户权限集合
 */
const val TOKEN_KEY_AUTHORITIES = "authorities"

/**
 * Token 存储字段名：token 过期时间
 */
const val TOKEN_KEY_EXPIRE_TIME = "expireTime"

@ConfigurationProperties("moyen.security")
class SecurityProperties
{
    /**
     * uri 白名单
     */
    var whiteList = arrayOf<String>()
}

@ConfigurationProperties("moyen.security.auth")
class AuthProperties
{
    /**
     * 解析和生成 token 使用的 key
     */
    var secretKey: String? = null

    /**
     * token 有效时长（单位：分钟）
     */
    var usableMinutes = 10080 // 7天

    /**
     * token 可刷新临期时间（单位：分钟），临期时间低于该值才可刷新
     */
    var refreshAllowTime = 1500 // 25小时
}

/**
 * 白名单匹配接口
 */
interface SecurityWhiteListMatcher : RequestMatcher

/**
 * 认证服务接口
 */
interface IAuthService<U : IUser> : UserDetailsService, IUserService<U>
{
    /**
     * 根据解析 token 得到的元数据生成当事人对象
     */
    fun <P : Principal> newPrincipal(claims: Claims): P

    /**
     * 根据用户对象生成 token
     */
    fun makeToken(): String

    override fun loadUserByUsername(username: String?) =
        throw UnsupportedOperationException("This method has been abandoned")
}

fun getAuth(): Authentication = SecurityContextHolder.getContext().authentication

fun setAuth(auth: Authentication)
{
    SecurityContextHolder.getContext().authentication = auth
}