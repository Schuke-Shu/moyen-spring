package cn.moyen.spring.core.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.PrintWriter
import java.io.StringWriter

// 通用工具
// Date: 2024-02-27 10:47

/**
 * 获取日志对象
 */
inline fun <reified T: Any> logger() = LoggerFactory.getLogger(T::class.java)!!

/**
 * 获取异常堆栈信息，并输出到日志
 */
fun logTraceInfo(logger: Logger, e: Throwable) =
    with(StringWriter()) {
        e.printStackTrace(PrintWriter(this, true))
        logger.error("\n$this")
    }