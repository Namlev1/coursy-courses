package com.coursy.courses

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CoursesApplication

fun main(args: Array<String>) {
    runApplication<CoursesApplication>(*args)
}
