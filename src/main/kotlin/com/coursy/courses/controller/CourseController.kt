package com.coursy.courses.controller

import com.coursy.courses.dto.CourseRequest
import com.coursy.courses.service.CourseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/courses")
class CourseController(
    val courseService: CourseService
) {

//    @GetMapping
//    fun getAllCourses(
//        @RequestParam(defaultValue = "0") page: Int,
//        @RequestParam(defaultValue = "10") size: Int,
//        @RequestParam(required = false) search: String?
//    ): ResponseEntity<Page<CourseDto>> {
//        TODO("Get paginated list of courses for current tenant with optional search")
//    }
//
//    @GetMapping("/{courseId}")
//    fun getCourseById(@PathVariable courseId: Long): ResponseEntity<CourseDetailsDto> {
//        TODO("Get detailed course information by ID")
//    }

    @PostMapping
    fun createCourse(
        @RequestBody courseRequest: CourseRequest,
        @AuthenticationPrincipal jwt: PreAuthenticatedAuthenticationToken
    ): ResponseEntity<Any> {
        val userEmail = jwt.principal as String
        val isUser = jwt.authorities
            .map { it.authority }
            .contains("ROLE_USER")

        val requestToValidate = if (isUser) {
            courseRequest.copy(email = userEmail)
        } else {
            courseRequest
        }

        return requestToValidate
            .validate()
            .fold(
                { validationErrors ->
                    ResponseEntity.badRequest().body(validationErrors)
                },
                { validatedRequest ->
                    courseService.saveCourse(validatedRequest)
                    ResponseEntity.status(HttpStatus.CREATED).build()
                }
            )
    }

//
//    @PutMapping("/{courseId}")
//    fun updateCourse(
//        @PathVariable courseId: Long,
//        @RequestBody @Valid updateCourseRequest: UpdateCourseRequest
//    ): ResponseEntity<CourseDto> {
//        TODO("Update existing course")
//    }
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