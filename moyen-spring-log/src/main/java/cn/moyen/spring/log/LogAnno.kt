package cn.moyen.spring.log

import cn.moyen.spring.log.enums.ServiceType
import cn.moyen.spring.log.enums.UserType

// Date: 2024-03-04 10:22

/**
 * 日志注解
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Log
    (
    /**
     * 业务模块
     */
    val title: String = "",
    /**
     * 描述
     */
    val description: String = "",
    /**
     * 业务类型
     */
    val serviceType: ServiceType = ServiceType.OTHER,
    /**
     * 操作人类型
     */
    val userType: UserType = UserType.ADMIN,
    /**
     * 是否记录请求参数
     */
    val recordParams: Boolean = true,
    /**
     * 是否记录相应结果
     */
    val recordResult: Boolean = true
)

/**
 * 日志序列化排除注解
 *
 * 标记一个类或属性，在打印日志时排除该元素
 *
 * 标注在 [Log] 标记的方法中的参数、参数对象内部属性、返回值对象内部属性上
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class LogIgnore