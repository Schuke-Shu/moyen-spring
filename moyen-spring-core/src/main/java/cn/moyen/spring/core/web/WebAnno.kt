package cn.schuke.website.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.annotation.AliasFor
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

// Date: 2024-02-27 17:27

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
@Validated
@AutoResult
@RestController
@RequestMapping
@Tag(name = "")
annotation class Web(
    @get:AliasFor(annotation = RestController::class, attribute = "value")
    val name: String = "",

    @get:AliasFor(annotation = RequestMapping::class)
    val path: String = "",

    @get:AliasFor(annotation = Tag::class, attribute = "name")
    val title: String = "",

    @get:AliasFor(annotation = Tag::class)
    val description: String = "",
)

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@Operation
@RequestMapping(method = [RequestMethod.GET])
annotation class GetApi(
    @get:AliasFor(annotation = RequestMapping::class)
    val path: Array<String> = [],

    @get:AliasFor(annotation = Operation::class, attribute = "summary")
    val title: String = "",

    @get:AliasFor(annotation = Operation::class)
    val parameters: Array<Parameter> = [],

    @get:AliasFor(annotation = Operation::class)
    val responses: Array<ApiResponse> = [],
)

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@Operation
@RequestMapping(method = [RequestMethod.POST])
annotation class PostApi(
    @get:AliasFor(annotation = RequestMapping::class)
    val path: Array<String> = [],

    @get:AliasFor(annotation = Operation::class, attribute = "summary")
    val title: String = "",

    @get:AliasFor(annotation = Operation::class)
    val parameters: Array<Parameter> = [],

    @get:AliasFor(annotation = Operation::class)
    val responses: Array<ApiResponse> = [],
)