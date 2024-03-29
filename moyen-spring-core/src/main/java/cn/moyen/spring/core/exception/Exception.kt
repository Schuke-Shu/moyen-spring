package cn.moyen.spring.core.exception

import cn.moyen.spring.core.web.ERR_UNKNOWN
import cn.moyen.spring.core.web.ServiceCode

// Date: 2024-02-27 13:55

class ServiceException(val code: ServiceCode = ERR_UNKNOWN, detail: String = "") : RuntimeException(detail)

open class SystemException : RuntimeException
{
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean) : super(
        message,
        cause,
        enableSuppression,
        writableStackTrace
    )
}

class ConfigException(message: String) : SystemException(message)