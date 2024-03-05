package cn.schuke.website.web

import cn.schuke.website.exception.ServiceException
import io.swagger.v3.oas.annotations.media.Schema

// Date: 2024-02-27 14:34

@Schema(name = "result", title = "JSON 响应数据", description = "Restful 风格 API 响应数据")
data class R<D>(
    @Schema(title = "响应码", description = "业务处理结果的响应状态码")
    val code: String,
    @Schema(title = "响应数据", description = "业务处理成功时返回的数据")
    val data: D,
    @Schema(title = "响应文本", description = "业务处理失败时返回的信息")
    val msg: String?
)

/**
 * 业务处理成功，无返回数据
 */
fun ok(): R<*> = R(OK.id, null, null)

/**
 * 业务处理成功，有返回数据
 */
fun <D> ok(data: D): R<D> =
    R<D>(OK.id, data, null)

/**
 * 业务处理失败
 */
fun fail(code: String, msg: String): R<*> =
    R(code, null, msg)

/**
 * 业务处理失败
 */
fun fail(code: ServiceCode): R<*> = fail(code.id, code.text)

/**
 * 业务处理失败
 */
fun fail(se: ServiceException): R<*> = fail(se.code)