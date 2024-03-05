package cn.moyen.spring.core.config

import cn.moyen.spring.core.MOYEN
import cn.moyen.spring.core.MOYEN_VERSION
import cn.moyen.spring.core.filter.RequestEntryFilter
import cn.moyen.spring.core.util.JSON
import cn.moyen.spring.core.util.logger
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.hibernate.validator.HibernateValidator
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.Ordered
import org.springframework.core.env.Environment
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

// Date: 2024-02-29 13:59

/**
 * 核心配置
 */
@Configuration
@EnableTransactionManagement
@PropertySource("classpath:/moyen-core.properties")
class CoreConfig : WebMvcConfigurer
{
    private val log = logger<CoreConfig>()

    init
    {
        log.info("Configuring moyen-core")
    }

    /**
     * 解决跨域问题
     */
    override fun addCorsMappings(registry: CorsRegistry) =
        registry.run {
            addMapping("/**").apply {
                allowedMethods("*")
                allowedHeaders("*")
                allowedOriginPatterns("*")
                allowCredentials(true)
                maxAge(3600)
            }
            log.debug("Configuring to allow cross-domain")
        }

    /**
     * 配置快速失败
     */
    @Bean
    fun validator(): Validator =
        Validation
            .byProvider(HibernateValidator::class.java)
            .configure()
            .failFast(true)
            .buildValidatorFactory()
            .validator
            .also { log.debug("Configuring fails-quickly") }

    @Bean
    fun json(): ObjectMapper = JSON

    @Bean
    @ConditionalOnMissingBean(OpenAPI::class)
    fun openApi(env: Environment): OpenAPI =
        OpenAPI().info(
            Info().apply {
                title = env.getProperty("spring.application.name")
                version = MOYEN_VERSION
                contact = Contact().apply {
                    name = MOYEN
                }
            }
        )

    /**
     * 配置请求入口过滤器
     */
    @Bean
    fun entryFilterRegistration(): FilterRegistrationBean<RequestEntryFilter> =
        FilterRegistrationBean<RequestEntryFilter>()
            .apply {
                setName("requestEntryFilter")
                filter = RequestEntryFilter()
                order = Ordered.HIGHEST_PRECEDENCE
            }
}