package cn.moyen.spring.data.config

import cn.moyen.spring.core.util.logger
import cn.moyen.spring.data.DataProperties
import cn.moyen.spring.data.interceptor.TimeInterceptor
import jakarta.annotation.PostConstruct
import org.apache.ibatis.session.SqlSessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

// Date: 2024-03-01 14:30

@Configuration
@PropertySource("classpath:/moyen-data.properties")
class DataConfig
{
    private val log = logger<DataConfig>()

    init
    {
        log.info("Configuring moyen-data")
    }

    @set:Autowired
    lateinit var properties: DataProperties

    @set:Autowired
    lateinit var factories: List<SqlSessionFactory>

    private val interceptor = TimeInterceptor()

    @PostConstruct
    fun addInterceptor() =
        factories
            .takeIf { properties.enableTimeInterceptor == true }
            ?.also { log.debug("Configuring mybatis interceptor") }
            ?.forEach { it.configuration.addInterceptor(interceptor) }
}