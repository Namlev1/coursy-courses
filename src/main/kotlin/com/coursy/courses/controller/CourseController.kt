package com.coursy.courses.controller

import com.coursy.courses.dto.CourseCreationRequest
import com.coursy.courses.dto.CourseUpdateRequest
import com.coursy.courses.security.AuthenticatedUser
import com.coursy.courses.service.CourseService
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
    ): ResponseEntity<Any> {
        return courseService
            .getById(courseId)
            .fold(
                { httpFailureResolver.handleFailure(it) },
                { ResponseEntity.ok(it) }
            )
    }

    @GetMapping("/page/{platformId}")
    fun getCourseList(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @PathVariable platformId: UUID,
    ): ResponseEntity<Any> =
        when {
            arePageParamsInvalid(page, size) -> ResponseEntity.badRequest().build()
            else -> PageRequest.of(page, size)
                .let { courseService.getPage(it, platformId) }
                .let { ResponseEntity.ok(it) }
        }


    @PreAuthorize("hasAnyRole('HOST_OWNER', 'HOST_ADMIN', 'PLATFORM_OWNER', 'PLATFORM_ADMIN')")
    @PostMapping
    fun createCourse(
        @RequestBody courseRequest: CourseCreationRequest,
        @AuthenticationPrincipal principal: AuthenticatedUser
    ): ResponseEntity<Any> {
        return courseRequest
            .validate()
            .fold(
                { httpFailureResolver.handleFailure(it) },
                { validatedRequest ->
                    courseService.save(validatedRequest, principal)
                        .fold(
                            { httpFailureResolver.handleFailure(it) },
                            { ResponseEntity.status(HttpStatus.CREATED).build() }
                        )
                }
            )
    }

    @PreAuthorize("hasAnyRole('HOST_OWNER', 'HOST_ADMIN', 'PLATFORM_OWNER', 'PLATFORM_ADMIN')")
    @PutMapping("/{courseId}")
    fun updateCourse(
        @PathVariable courseId: UUID,
        @RequestBody courseRequest: CourseUpdateRequest,
        @AuthenticationPrincipal principal: AuthenticatedUser
    ): ResponseEntity<Any> {
        return courseRequest
            .validate()
            .fold(
                { httpFailureResolver.handleFailure(it) },
                { validatedRequest ->
                    courseService.update(courseId, validatedRequest, principal)
                        .fold(
                            { httpFailureResolver.handleFailure(it) },
                            { course -> ResponseEntity.ok(course) }
                        )
                }
            )
    }

    @PreAuthorize("hasAnyRole('HOST_OWNER', 'HOST_ADMIN', 'PLATFORM_OWNER', 'PLATFORM_ADMIN')")
    @DeleteMapping("/{courseId}")
    fun deleteCourse(
        @PathVariable courseId: UUID,
        @AuthenticationPrincipal principal: AuthenticatedUser
    ): ResponseEntity<Any> {
        return courseService
            .deleteById(courseId, principal)
            .fold(
                { httpFailureResolver.handleFailure(it) },
                { ResponseEntity.status(HttpStatus.NO_CONTENT).build() }
            )
    }

    private fun arePageParamsInvalid(page: Int, size: Int) =
        page < 0 || size <= 0
}