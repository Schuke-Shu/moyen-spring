package cn.moyen.spring.cache

import cn.moyen.spring.core.util.logger
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.cache.RedisCacheWriter
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.concurrent.TimeUnit

// Date: 2024-03-01 18:36

// 默认 15 分钟
const val DEFAULT_SECONDS: Long = 15 * 60

// 默认时间单位为秒
private val DEFAULT_TIME_UNIT = TimeUnit.SECONDS

internal lateinit var redis: RedisTemplate<String, Any>

/**
 * 设置缓存超时时间
 */
fun expire(key: String, time: Long, unit: TimeUnit = DEFAULT_TIME_UNIT) = redis.expire(key, time, unit)

/**
 * 设置缓存，同时设置有效时间
 */
fun set(key: String, value: Any, seconds: Long = DEFAULT_SECONDS) = ops().set(key, value, seconds, DEFAULT_TIME_UNIT)

/**
 * 删除缓存
 */
fun remove(key: String) = redis.delete(key)

/**
 * 获取缓存
 */
fun get(key: String) = ops().get(key)

/**
 * key 是否存在
 */
fun has(key: String) = redis.hasKey(key)

private fun ops() = redis.opsForValue()

@Configuration
class CacheConfig
{
    private val log = logger<CacheConfig>()

    init
    {
        log.info("Configuring moyen-cache")
    }

    @Bean
    fun redisTemplate(factory: RedisConnectionFactory) =
        RedisTemplate<String, Any>().apply {
            log.debug("Configuring redis-template")
            connectionFactory = factory
            val cacheJson = JsonMapper.builder().apply {
                serializationInclusion(JsonInclude.Include.NON_EMPTY)
                activateDefaultTyping(
                    // 使用注解存储缓存时，spring 在取缓存时若没有类型标记，会转换为 LinkedHashMap，造成类型转换异常，所以必须携带类型
                    LaissezFaireSubTypeValidator.instance,
                    ObjectMapper.DefaultTyping.NON_FINAL,
                    JsonTypeInfo.As.WRAPPER_ARRAY
                )
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            }.build()

            // Json 序列化
            val jsonSerializer = Jackson2JsonRedisSerializer(cacheJson, Any::class.java)

            // String 序列化
            val strSerializer = StringRedisSerializer()

            keySerializer = strSerializer
            hashKeySerializer = strSerializer
            stringSerializer = strSerializer
            valueSerializer = jsonSerializer
            hashValueSerializer = jsonSerializer
            afterPropertiesSet()

            redis = this
        }

    @Bean
    fun redisCacheManager(factory: RedisConnectionFactory, redisTemplate: RedisTemplate<String, Any>) =
        log.run {
            debug("Configuring redis-cache-manager")
            RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(factory),
                RedisCacheConfiguration.defaultCacheConfig().apply {
                    serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.stringSerializer)
                    )
                    serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.valueSerializer)
                    )
                }
            )
        }
}

@NoRepositoryBean
interface LongIdCacheRepository<C> : CrudRepository<C, Long>

@NoRepositoryBean
interface StringIdCacheRepository<C> : CrudRepository<C, String>