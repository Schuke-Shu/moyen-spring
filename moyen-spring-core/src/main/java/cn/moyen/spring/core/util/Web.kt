package cn.moyen.spring.util

import cn.jruyi.util.StringUtil
import jakarta.servlet.http.HttpServletRequest
import org.springframework.util.FileCopyUtils
import java.io.InputStream
import java.net.InetAddress

// Date: 2024-02-27 14:57

@JvmField
val HEADERS = arrayOf(
    "Proxy-Client-IP",
    "WL-Proxy-Client-IP",
    "HTTP_CLIENT_IP",
    "HTTP_X_FORWARDED_FOR",
    "X-Real-IP"
)

const val LOCAL_IP4 = "127.0.0.1"

/**
 * 获取请求来源的 ip
 */
fun getRequestIp() = getRequestIp(getRequest())

/**
 * 获取请求来源的 ip
 */
fun getRequestIp(request: HttpServletRequest): String =
    with(request.getHeader("x-forwarded-for"))
    {
        if (ipNotEmpty(this))
        {
            if (this.contains(",")) this.split(",")[0]
            else this
        }
        else
        {
            for (header in HEADERS)
                request
                    .getHeader(header)
                    .run { if (ipNotEmpty(this)) return@with this }

            request.remoteAddr.run {
                if (ipNotEmpty(this)) this
                else LOCAL_IP4
            }
        }
    }

private fun ipNotEmpty(ip: String?): Boolean = !StringUtil.isBlank(ip) && !"unknown".equals(ip, true)

/**
 * 获取本机网络 ip
 */
fun getLocalIp(): String = InetAddress.getLocalHost().hostAddress

/**
 * 向客户端返回文件
 */
fun resFile(filename: String, input: InputStream) =
    getResponse().run {
        contentType = "application/force-download"
        setHeader("Content-Disposition", "attachment;filename=$filename")
        FileCopyUtils.copy(input, outputStream)
    }

/**
 * 向客户端返回 json
 */
fun resJson(data: Any) =
    getResponse().run {
        contentType = "application/json;charset=utf-8"
        writer.use { w ->
            w.write(toJson(data))
            w.flush()
        }
    }