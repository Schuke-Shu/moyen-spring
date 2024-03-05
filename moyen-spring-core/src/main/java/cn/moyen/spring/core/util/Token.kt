package cn.moyen.spring.core.util

import cn.jruyi.util.Assert
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.time.Instant
import java.util.*
import java.util.function.Consumer

// token 工具
// Date: 2024-02-27 20:49

/**
 * token 签名算法
 */
const val TOKEN_ALGORITHM = "HS256"

/**
 * token 类型
 */
const val TOKEN_TYPE = "JWT"


/**
 * 生成 token
 */
fun token(
    claimsHandler: Consumer<Map<String, Any>>, expire: Long, secretKey: String,
    algorithm: String = TOKEN_ALGORITHM, type: String = TOKEN_TYPE
): String =
    HashMap<String, Any>().run {
        Assert.notBlank(secretKey, "invalid token secret-key")
        Assert.isTrue(expire < System.currentTimeMillis(), "invalid token expire time")
        claimsHandler.accept(this)

        with(Jwts.builder())
        {
            setHeaderParam("alg", algorithm)
            setHeaderParam("typ", type)
            setClaims(this@run)

            setExpiration(Date.from(Instant.ofEpochMilli(expire)))
            signWith(
                SignatureAlgorithm.forName(algorithm),
                secretKey
            )

            compact()
        }
    }

/**
 * 解析token
 */
fun parseToken(token: String, securityKey: String): Claims =
    with(Jwts.parser())
    {
        setSigningKey(securityKey)
        parseClaimsJws(token).body
    }