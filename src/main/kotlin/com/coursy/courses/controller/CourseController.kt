package com.coursy.courses.controller

import com.coursy.courses.dto.CourseCreationRequest
import com.coursy.courses.dto.CourseUpdateRequest
import com.coursy.courses.service.CourseService
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/courses")
class CourseController(
    val courseService: CourseService,
    val httpFailureResolver: HttpFailureResolver
) {

    @GetMapping("/{courseId}")
    fun getCourse(
        @PathVariable courseId: UUID,
        jwt: PreAuthenticatedAuthenticationToken
    ): ResponseEntity<Any> {
        return courseService
            .getById(courseId, jwt)
            .fold(
                { httpFailureResolver.handleFailure(it) },
                { ResponseEntity.ok(it) }
            )
    }

    @GetMapping
    fun getCourseList(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ResponseEntity<Any> =
        when {
            arePageParamsInvalid(page, size) -> ResponseEntity.badRequest().build()
            else -> PageRequest.of(page, size)
                .let { courseService.getPage(it) }
                .let { ResponseEntity.ok(it) }
        }

    @PostMapping
    fun createCourse(
        @RequestBody courseRequest: CourseCreationRequest,
        jwt: PreAuthenticatedAuthenticationToken
    ): ResponseEntity<Any> {
        return courseRequest
            .validate()
            .fold(
                { httpFailureResolver.handleFailure(it) },
                { validatedRequest ->
                    courseService.save(validatedRequest, jwt)
                        .fold(
                            { httpFailureResolver.handleFailure(it) },
                            { ResponseEntity.status(HttpStatus.CREATED).build() }
                        )
                }
            )
    }


    @PutMapping("/{courseId}")
    fun updateCourse(
        @PathVariable courseId: UUID,
        @RequestBody courseRequest: CourseUpdateRequest,
        jwt: PreAuthenticatedAuthenticationToken
    ): ResponseEntity<Any> {
        return courseRequest
            .validate()
            .fold(
                { httpFailureResolver.handleFailure(it) },
                { validatedRequest ->
                    courseService.update(courseId, validatedRequest, jwt)
                        .fold(
                            { httpFailureResolver.handleFailure(it) },
                            { course -> ResponseEntity.ok(course) }
                        )
                }
            )
    }

    @DeleteMapping("/{courseId}")
    fun deleteCourse(
        @PathVariable courseId: UUID,
        jwt: PreAuthenticatedAuthenticationToken
    ): ResponseEntity<Any> {
        return courseService
            .deleteById(courseId, jwt)
            .fold(
                { httpFailureResolver.handleFailure(it) },
                { ResponseEntity.status(HttpStatus.NO_CONTENT).build() }
            )
    }

    private fun arePageParamsInvalid(page: Int, size: Int) =
        page < 0 || size <= 0
}