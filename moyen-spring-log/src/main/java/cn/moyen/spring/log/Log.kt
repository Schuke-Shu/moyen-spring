package cn.moyen.spring.log

import cn.moyen.spring.core.util.logger
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import com.fasterxml.jackson.databind.json.JsonMapper
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// Date: 2024-03-04 10:06

internal val LOG_JSON =
    JsonMapper.builder()
        .run {
            serializationInclusion(JsonInclude.Include.NON_EMPTY)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            annotationIntrospector(
                object : JacksonAnnotationIntrospector()
                {
                    override fun _isIgnorable(a: com.fasterxml.jackson.databind.introspect.Annotated?) =
                        if (_findAnnotation(a, LogIgnore::class.java) !== null) true else super._isIgnorable(a)
                }
            )
        }
        .build()

@Configuration
class LogConfig
{
    private val log = logger<LogConfig>()

    init
    {
        log.info("Configuring moyen-log")
    }

    @Bean
    @ConditionalOnMissingBean(ILogService::class)
    fun nullLogService() = object : ILogService
    {
        init
        {
            log.info("Initializing default log service implementation")
        }

        override fun record(sysLog: SysLog) =
            if (log.isInfoEnabled) log.info("Logging: $sysLog") else Unit
    }
}

/**
 * 日志服务接口
 */
interface ILogService
{
    /**
     * 记录一条日志
     */
    fun record(sysLog: SysLog)
}