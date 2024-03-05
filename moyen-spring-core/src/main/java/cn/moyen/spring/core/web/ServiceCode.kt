package cn.schuke.website.web

import io.swagger.v3.oas.annotations.media.Schema
import java.util.function.Consumer

// Date: 2024-02-27 16:56

@Schema(description = "业务状态码")
data class ServiceCode(
    @Schema(title = "状态码标识", description = "十六进制字符串")
    val id: String,
    @Schema(title = "状态码信息", description = "状态码描述信息")
    val text: String
)

@JvmField
internal val SERVICE_CODES: Set<ServiceCode> = LinkedHashSet()

@JvmField
val OK = registerCode("0", "success")

@JvmField
val ERR_UNKNOWN = registerCode("1", "unknown error")

// 其他错误，用于日志记录
@JvmField
val ERR_OTHER = registerCode("2", "other error")

// - 用户端错误
@JvmField
val ERR_USER = registerCode("A0001", "user-end error")

@JvmField
val ERR_REGISTER = registerCode("A0100", "user register error")

@JvmField
val ERR_LOGIN = registerCode("A0200", "abnormal user login")

@JvmField
val ERR_AUTH = registerCode("A0220", "abnormal user authentication")

@JvmField
val ERR_TOKEN_EXPIRED = registerCode("A0221", "user login-token expired")

@JvmField
val ERR_TOKEN_SIGNATURE = registerCode("A0222", "user login-token signature error")

@JvmField
val ERR_TOKEN_MALFORMED = registerCode("A0223", "user login-token format error")

@JvmField
val ERR_PERMISSION = registerCode("A0300", "abnormal user permissions")

@JvmField
val ERR_REQUEST_PARAMS = registerCode("A0400", "abnormal user request parameter")

// - 系统端错误
@JvmField
val ERR_SYSTEM = registerCode("B0000", "system-end error")

fun registerCode(id: String, text: String): ServiceCode = ServiceCode(id, text).apply { SERVICE_CODES.plus(this) }

fun doWithServiceCodes(consumer: Consumer<ServiceCode>) = SERVICE_CODES.forEach(consumer)