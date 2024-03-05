package cn.moyen.spring.data

import cn.moyen.spring.core.util.logger
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

// Date: 2024-03-01 14:30

@Configuration
@MapperScan("**.mapper")
@PropertySource("classpath:/moyen-data.properties")
class DataConfig
{
    private val log = logger<DataConfig>()

    init
    {
        log.info("Configuring moyen-data")
    }
}