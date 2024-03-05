package cn.moyen.spring.core.util

import cn.moyen.spring.core.exception.SystemException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

// Date: 2024-02-27 10:51

/**
 * 获取请求属性
 */
fun getRequestAttributes(): ServletRequestAttributes =
    RequestContextHolder.getRequestAttributes() as ServletRequestAttributes

/**
 * 获取当前请求的 request 对象
 */
fun getRequest(): HttpServletRequest = getRequestAttributes().request

/**
 * 获取当前请求的 response 对象
 */
fun getResponse(): HttpServletResponse = getRequestAttributes().response ?: throw SystemException("null response")

/**
 * 获取当前请求的方法
 */
fun getRequestMethod(): String = getRequest().method

/**
 * 获取当前请求的 uri
 */
fun getRequestURI(): String = getRequest().requestURI

/**
 * 根据 key 获取请求头
 */
fun getRequestHeader(key: String): String = getRequest().getHeader(key)

/**
 * 获取请求参数 map
 */
fun getRequestParameterMap(): Map<String, Array<String>> = getRequest().parameterMap

/**
 * 根据 key 获取请求参数
 */
fun getRequestParameter(key: String): String = getRequest().getParameter(key)

/**
 * 获取请求属性
 */
fun getRequestAttribute(key: String): Any = getRequest().getAttribute(key)

/**
 * 设置请求属性
 */
fun setRequestAttribute(key: String, value: Any) = getRequest().setAttribute(key, value)