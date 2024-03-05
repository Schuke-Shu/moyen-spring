package cn.moyen.spring.core.exception

import cn.moyen.spring.core.util.logTraceInfo
import cn.moyen.spring.core.util.logger
import cn.moyen.spring.core.web.*
import jakarta.validation.ConstraintViolationException
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.util.*

// Date: 2024-02-28 13:49

/**
 * 全局异常处理器
 */
@RestControllerAdvice
class GlobalExceptionHandler
{
    private val log = logger<GlobalExceptionHandler>()

    init
    {
        log.debug("Initializing global-exception-handler")
    }

    // 业务异常
    @ExceptionHandler(ServiceException::class)
    fun handleServiceException(e: ServiceException): R<*> =
        log.run { debug("-- service exception: {}", e.code); fail(e) }

    // 系统异常
    @ExceptionHandler(SystemException::class)
    fun handleSystemException(e: SystemException): R<*> =
        log.run { debug("-- system exception: {}", e.message); fail(ERR_SYSTEM) }

    // 数据验证异常
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleValidException(e: ConstraintViolationException): R<*> = fail(ERR_REQUEST_PARAMS)
        .also {
            log.run {
                if (isDebugEnabled)
                    debug(
                        "-- data validation exception: {}\n{}", e.javaClass.typeName,
                        StringJoiner(", ", "[", "]").apply {
                            for (v in e.constraintViolations) add("{${v.propertyPath}: ${v.invalidValue}, ${v.message}}")
                        }
                    )
            }
        }

    // 数据验证异常
    @ExceptionHandler(BindException::class, MethodArgumentNotValidException::class)
    fun handleValidException(e: java.lang.Exception): R<*> = fail(ERR_REQUEST_PARAMS)
        .also {
            log.run {
                if (isDebugEnabled)
                    debug("-- data validation exception: {}\n{}", e.javaClass.typeName, e.message)
            }
        }

    // 资源不存在异常
    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(e: NoResourceFoundException): R<*> = fail(ERR_REQUEST_PARAMS)
        .also {
            log.run { debug("-- resource not found\n{}", e.message) }
        }


    // 未处理异常
    @ExceptionHandler(Throwable::class)
    fun handleUnhandledException(e: Throwable): R<*> = fail(ERR_UNKNOWN).also { logTraceInfo(log, e) }
}