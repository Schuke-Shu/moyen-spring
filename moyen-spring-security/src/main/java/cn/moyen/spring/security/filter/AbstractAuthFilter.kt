package cn.moyen.spring.security.filter

import cn.jruyi.util.Util
import cn.moyen.spring.core.exception.ServiceException
import cn.moyen.spring.core.util.resJson
import cn.moyen.spring.core.web.ERR_UNKNOWN
import cn.moyen.spring.core.web.fail
import io.jsonwebtoken.Claims
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

// Date: 2024-03-04 14:58

abstract class AbstractAuthFilter : OncePerRequestFilter()
{
    protected val log = logger

    init
    {
        log.debug("Initializing auth-filter")
    }

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain)
    {
        // 清空 SecurityContext，强制所有请求都必须通过认证
        SecurityContextHolder.clearContext()

        val claims: Claims?
        try
        {
            claims = getClaims(req, res)
        }
        catch (e: Throwable)
        {
            resJson(fail(if (e is ServiceException) e.code else ERR_UNKNOWN))
            return
        }

        if (Util.isEmpty(claims))
        {
            log.debug("invalid auth info, released")
            chain.doFilter(req, res)
        }
        else
        {
            saveAuth(claims!!)

            // 放行
            log.debug("Token parsed successfully, released")
            chain.doFilter(req, res)
        }
    }

    /**
     * 获取认证信息
     */
    protected abstract fun getClaims(req: HttpServletRequest, res: HttpServletResponse): Claims?

    /**
     * 存储认证结果
     */
    protected abstract fun saveAuth(claims: Claims)
}