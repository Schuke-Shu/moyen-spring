package cn.moyen.spring.core.util

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper

internal val JSON =
    JsonMapper.builder()
        .apply {
            visibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            serializationInclusion(JsonInclude.Include.NON_EMPTY)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }
        .build()

/**
 * 将实例转换为 json 字符串
 */
fun toJson(arg: Any, mapper: ObjectMapper = JSON): String = mapper.writeValueAsString(arg)

/**
 * 将 json 字符串转换为实例
 */
fun <T> readJson(json: CharSequence, type: Class<T>, mapper: ObjectMapper = JSON): T =
    mapper.readValue(json.toString(), type)

/**
 * 将 json 字符串转换为实例
 */
fun <T> readJson(json: CharSequence, ref: TypeReference<T>, mapper: ObjectMapper = JSON): T =
    mapper.readValue(json.toString(), ref)