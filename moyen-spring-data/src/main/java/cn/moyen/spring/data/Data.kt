package cn.moyen.spring.data

import org.springframework.boot.context.properties.ConfigurationProperties

// Date: 2024-03-01 16:36

@ConfigurationProperties("moyen.data")
class DataProperties
{
    var enableTimeInterceptor: Boolean? = true
}