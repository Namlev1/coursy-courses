package com.coursy.courses.controller

import com.coursy.courses.dto.CourseCreationRequest
import com.coursy.courses.dto.CourseUpdateRequest
import com.coursy.courses.service.CourseService
import com.coursy.courses.service.JwtService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.web.bind.annotation.*
import java.util.*

// todo improve error handling
@RestController
@RequestMapping("/api/courses")
class CourseController(
    val courseService: CourseService,
    val jwtService: JwtService
) {

//    @GetMapping
//    fun getAllCourses(
//        @RequestParam(defaultValue = "0") page: Int,
//        @RequestParam(defaultValue = "10") size: Int,
//        @RequestParam(required = false) search: String?
//    ): ResponseEntity<Page<CourseDto>> {
//        TODO("Get paginated list of courses for current tenant with optional search")
//    }

    @GetMapping("/{courseId}")
    fun getCourse(
        @PathVariable courseId: UUID,
        jwt: PreAuthenticatedAuthenticationToken
    ): ResponseEntity<Any> {
        return courseService
            .getById(courseId, jwt)
            .fold(
                { ResponseEntity.badRequest().body(it.message()) },
                { ResponseEntity.ok(it) }
            )
    }

    @PostMapping
    fun createCourse(
        @RequestBody courseRequest: CourseCreationRequest,
        jwt: PreAuthenticatedAuthenticationToken
    ): ResponseEntity<Any> {
        return courseRequest
            .validate()
            .fold(
                { validationErrors ->
                    ResponseEntity.badRequest().body(validationErrors)
                },
                { validatedRequest ->
                    courseService.saveCourse(validatedRequest, jwt)
                        .fold(
                            { ResponseEntity.badRequest().body(it.message()) },
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
                { validationErrors ->
                    val messages = validationErrors.map { it.message() }
                    ResponseEntity.badRequest().body(mapOf("errors" to messages))
                },
                { validatedRequest ->
                    courseService.update(courseId, validatedRequest, jwt)
                        .fold(
                            { failure -> ResponseEntity.badRequest().body(failure.message()) },
                            { course -> ResponseEntity.ok(course) }
                        )
                }
            )
    }
//
//    @DeleteMapping("/{courseId}")
//    fun deleteCourse(@PathVariable courseId: Long): ResponseEntity<Void> {
//        TODO("Delete course (soft delete)")
//    }
//
//    @PatchMapping("/{courseId}/publish")
//    fun publishCourse(@PathVariable courseId: Long): ResponseEntity<CourseDto> {
//        TODO("Publish course (make it available to students)")
//    }
//
//    @PatchMapping("/{courseId}/unpublish")
//    fun unpublishCourse(@PathVariable courseId: Long): ResponseEntity<CourseDto> {
//        TODO("Unpublish course (hide from students)")
//    }
}