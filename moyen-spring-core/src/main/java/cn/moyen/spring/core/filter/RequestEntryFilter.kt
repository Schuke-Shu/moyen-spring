package cn.schuke.website.filter

import cn.schuke.website.DEFAULT_DATETIME_FORMATTER
import cn.schuke.website.REQUEST_TIME
import cn.schuke.website.util.getRequestIp
import cn.schuke.website.util.logger
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.PriorityOrdered
import org.springframework.web.filter.OncePerRequestFilter
import java.time.LocalDateTime

// Date: 2024-02-27 21:15

/**
 * 请求入口过滤器
 *
 * 该过滤器有最高优先级
 */
class RequestEntryFilter : OncePerRequestFilter(), PriorityOrdered
{
    private val log = logger<RequestEntryFilter>()

    init
    {
        log.debug("Initializing request-entry-filter")
    }

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) =
        chain.run {
            if (req.requestURI == "/favicon.ico") Unit
            else
                log.run {
                    val now = System.currentTimeMillis()
                    if (isDebugEnabled)
                        debug(
                            """
                        
                        accept [${req.method}] request:
                                  IP: ${getRequestIp(req)}
                                 URI: ${req.requestURI}
                                Time: ${LocalDateTime.now().format(DEFAULT_DATETIME_FORMATTER)}
                        Content Type: ${req.contentType}
                    """.trimIndent()
                        )

                    req.setAttribute(REQUEST_TIME, now)
                    doFilter(req, res)
                }
        }

    override fun getOrder(): Int = Ordered.HIGHEST_PRECEDENCE
}