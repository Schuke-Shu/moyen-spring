package cn.moyen.spring.core.config

import cn.moyen.spring.core.DEFAULT_DATETIME_FORMATTER
import cn.moyen.spring.core.util.getLocalIp
import cn.moyen.spring.core.util.logger
import org.springframework.boot.SpringApplication
import org.springframework.boot.SpringApplicationRunListener
import org.springframework.context.ConfigurableApplicationContext
import java.time.Duration
import java.time.LocalDateTime

// Date: 2024-02-29 20:23

class BootListener(app: SpringApplication, args: Array<String>) : SpringApplicationRunListener
{
    private val log = logger<BootListener>()

    override fun ready(context: ConfigurableApplicationContext, timeTaken: Duration): Unit =
        log.run {
            val protocol = if (context.environment.getProperty("server.ssl.enable").toBoolean()) "https" else "http"
            val port = context.environment.getProperty("local.server.port")
            info(
                Welcome(
                    context.environment.getProperty("spring.application.name"),
                    timeTaken.toMillis().div(1000.0),
                    LocalDateTime.now().format(DEFAULT_DATETIME_FORMATTER),
                    "$protocol://${getLocalIp()}:$port",
                    context.beanDefinitionCount
                )
                    .toString()
            )
        }
}