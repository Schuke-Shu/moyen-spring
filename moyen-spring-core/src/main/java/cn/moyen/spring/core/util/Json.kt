package cn.moyen.spring.util

import cn.moyen.spring.constant.JSON
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * 将实例转换为 json 字符串
 */
fun toJson(mapper: ObjectMapper, arg: Any): String = mapper.writeValueAsString(arg)

/**
 * 将实例转换为 json 字符串
 */
fun toJson(arg: Any): String = JSON.writeValueAsString(arg)

/**
 * 将 json 字符串转换为实例
 */
fun <T> readJson(mapper: CharSequence, type: Class<T>): T = readJson(JSON, mapper, type)

/**
 * 将 json 字符串转换为实例
 */
fun <T> readJson(mapper: ObjectMapper, json: CharSequence, type: Class<T>): T = mapper.readValue(json.toString(), type)

/**
 * 将 json 字符串转换为实例
 */
fun <T> readJson(json: CharSequence, ref: TypeReference<T>): T = JSON.readValue(json.toString(), ref)

/**
 * 将 json 字符串转换为实例
 */
fun <T> readJson(mapper: ObjectMapper, json: CharSequence, ref: TypeReference<T>): T = mapper.readValue(json.toString(), ref)