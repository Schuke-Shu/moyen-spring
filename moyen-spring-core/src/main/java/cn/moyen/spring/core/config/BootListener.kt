package cn.schuke.website.config

import cn.schuke.website.DEFAULT_DATETIME_FORMATTER
import cn.schuke.website.util.getLocalIp
import cn.schuke.website.util.logger
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
            val port = context.environment.getProperty("server.port") ?: "8080"
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

            System.getProperties().forEach { println("${it.key}: ${it.value}")}
            println("=================================")
            val env = context.environment
            println()
        }
}