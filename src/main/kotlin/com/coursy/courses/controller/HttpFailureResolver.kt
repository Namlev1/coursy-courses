package com.coursy.courses.controller

import arrow.core.Nel
import com.coursy.courses.failure.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class HttpFailureResolver {
    fun handleFailure(failure: Failure): ResponseEntity<Any> =
        when (failure) {
            // Validation
            is ValidationFailure -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failure.message())

            // Authorization
            is AuthorizationFailure -> ResponseEntity.status(HttpStatus.FORBIDDEN).body(failure.message())

            // Courses
            is CourseFailure.NotFound -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(failure.message())

            // UserCourses
            is UserCourseFailure.NotFound -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(failure.message())
            is UserCourseFailure.NotFoundByUserAndVideo -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(failure.message())

            else -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failure.message())
        }

    fun handleFailure(failures: Nel<Failure>): ResponseEntity<Any> =
        ResponseEntity.badRequest().body(mapOf("errors" to failures.map { it.message() }))
}
