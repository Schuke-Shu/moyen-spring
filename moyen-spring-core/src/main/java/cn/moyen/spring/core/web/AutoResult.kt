package cn.moyen.spring.core.web

import cn.moyen.spring.core.util.logger
import cn.moyen.spring.core.util.toJson
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

// Date: 2024-02-27 17:36

/**
 * 自动打包结果注解
 *
 * 被该注解标记的控制器的返回值会被自动打包为 R 结果类
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class AutoResult

/**
 * 请求结果打包器
 */
@RestControllerAdvice(annotations = [AutoResult::class])
class AutoResultPacker : ResponseBodyAdvice<Any>
{
    private val log = logger<AutoResultPacker>()

    init
    {
        log.debug("Initializing auto-result-packer")
    }

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean =
        true

    override fun beforeBodyWrite(
        body: Any?, returnType: MethodParameter, selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>?>, request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any =
        log.run {
            if (body is R<*>) body
            else
            {
                if (isDebugEnabled)
                    debug(
                        "Request success".apply {
                            if (body !== null) plus("\nResult: $body")
                        }
                    )

                if (selectedConverterType.isAssignableFrom(StringHttpMessageConverter::class.java))
                    toJson(ok(body))
                else
                    ok(body)
            }
        }
}