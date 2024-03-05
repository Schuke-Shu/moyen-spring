package cn.moyen.spring.security.filter

import cn.jruyi.util.StringUtil
import cn.moyen.spring.core.exception.ServiceException
import cn.moyen.spring.core.util.logTraceInfo
import cn.moyen.spring.core.util.logger
import cn.moyen.spring.core.util.parseToken
import cn.moyen.spring.core.util.readJson
import cn.moyen.spring.core.web.ERR_TOKEN_EXPIRED
import cn.moyen.spring.core.web.ERR_UNKNOWN
import cn.moyen.spring.security.*
import cn.moyen.spring.security.entity.AuthToken
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.security.core.authority.SimpleGrantedAuthority

// Date: 2024-03-04 15:10

@ConditionalOnMissingBean(AbstractAuthFilter::class)
class TokenFilter : AbstractAuthFilter()
{
    init
    {
        log.debug("Initializing token parsing authentication filter")
    }

    @set:Autowired
    lateinit var auths: IAuthService<*>

    @set:Autowired
    lateinit var props: AuthProperties

    override fun getClaims(req: HttpServletRequest, res: HttpServletResponse): Claims? =
        req.getHeader(AUTH_HEADER).run {
            if (isInvalid(this)) null
            else
                try
                {
                    parseToken(this, props.secretKey!!)
                }
                catch (e: Exception)
                {
                    when (e)
                    {
                        is ExpiredJwtException -> throw ServiceException(ERR_TOKEN_EXPIRED)
                        else -> log.run {
                            debug("Token parsing failed with an unknown error")
                            logTraceInfo(logger<TokenFilter>(), e)
                            throw ServiceException(ERR_UNKNOWN)
                        }
                    }
                }
        }

    override fun saveAuth(claims: Claims) =
        setAuth(
            AuthToken(
                // 从 Claims 中获取数据，并封装 Principal（当事人）对象
                auths.newPrincipal(claims),
                // 解析 json 形式的权限集合
                readJson(
                    claims[TOKEN_KEY_AUTHORITIES, String::class.java],
                    Array<SimpleGrantedAuthority>::class.java
                ).toList()
            )
        )

    private fun isInvalid(token: String?): Boolean =
        token === null || token.length < TOKEN_MIN_LENGTH || !StringUtil.hasText(token)
}