package cn.moyen.spring.security.config

import cn.moyen.spring.core.util.logger
import cn.moyen.spring.security.LOGIN_URI
import cn.moyen.spring.security.REGISTER_URI
import cn.moyen.spring.security.SecurityProperties
import cn.moyen.spring.security.SecurityWhiteListMatcher
import cn.moyen.spring.security.filter.AbstractAuthFilter
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import java.util.*

// Date: 2024-03-04 13:31

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
class SecurityConfig
{
    private val log = logger<SecurityConfig>()

    init
    {
        log.info("Configuring moyen-security")
    }

    /**
     * 加密编码器
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder::class)
    fun passwordEncoder() = BCryptPasswordEncoder()

    /**
     * 固定白名单
     */
    internal val fixedWhiteList = arrayOf("$REGISTER_URI/**", "$LOGIN_URI/**", "favicon.ico")

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain::class)
    fun securityFilterChain(http: HttpSecurity, filter: AbstractAuthFilter, properties: SecurityProperties): SecurityFilterChain =
        http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        *ServiceLoader
                            .load(SecurityWhiteListMatcher::class.java)
                            .stream()
                            .map { p -> p.get() }
                            .toArray { i -> arrayOfNulls<RequestMatcher>(i) }
                    )
                    .permitAll()
                    .requestMatchers(*fixedWhiteList)
                    .permitAll()
                    .requestMatchers(*properties.whiteList)
                    .permitAll()
                    // 其他任何请求需要通过认证
                    .anyRequest()
                    .authenticated()
            }
            // 启用 Security 框架自带的 CorsFilter 过滤器，对 OPTIONS 请求放行
            .cors(Customizer.withDefaults<CorsConfigurer<HttpSecurity>>())
            // 禁用“防止伪造的跨域攻击”防御机制
            .csrf { it.disable() }
            .formLogin { it.disable() }
            // 设置 Session 创建策略：从不创建
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER) }
            // 用 token 过滤器替换 Spring Security 的“用户名密码认证信息过滤器”
            .addFilterAt(filter, UsernamePasswordAuthenticationFilter::class.java)
            .headers { config -> config.cacheControl { it.disable() } }
            .build()
}