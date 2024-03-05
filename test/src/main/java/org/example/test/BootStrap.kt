package org.example.test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// Date: 2024-03-01 17:42

@SpringBootApplication
class BootStrap

fun main(vararg args: String)
{
    val env = runApplication<BootStrap>(*args).environment
    System.getProperties().forEach { (k, v) -> println("$k: $v") }
}