package com.coursy.courses.controller

import com.coursy.courses.dto.UserCourseDto
import com.coursy.courses.security.AuthenticatedUser
import com.coursy.courses.service.UserCourseService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/courses/user-courses")
class UserCourseController(
    val userCourseService: UserCourseService,
    val httpFailureResolver: HttpFailureResolver
) {
    @PreAuthorize("hasAnyRole('HOST_OWNER', 'HOST_ADMIN', 'PLATFORM_OWNER', 'PLATFORM_ADMIN')")
    @GetMapping("/user/{id}")
    fun getUserCourses(
        @PathVariable id: UUID
    ): ResponseEntity<List<UserCourseDto>> {
        return userCourseService
            .getByUserId(id)
            .let { ResponseEntity.ok(it) }
    }

    @GetMapping("/me")
    fun getCurrentUserCourses(
        @AuthenticationPrincipal principal: AuthenticatedUser
    ): ResponseEntity<List<UserCourseDto>> {
        return userCourseService
            .getByUserId(principal.id)
            .let { ResponseEntity.ok(it) }
    }

    @GetMapping("/{id}")
    fun getUserCourse(
        @PathVariable id: UUID
    ): ResponseEntity<Any> {
        return userCourseService
            .getByUserCourseId(id)
            .fold(
                { httpFailureResolver.handleFailure(it) },
                { userCourse -> ResponseEntity.ok(userCourse) }
            )
    }

    @PostMapping
    fun addUserCourse(
        @RequestBody dto: UserCourseDto
    ): ResponseEntity<Any> {
        return userCourseService
            .createUserCourse(dto)
            .fold(
                { httpFailureResolver.handleFailure(it) },
                { userCourse -> ResponseEntity.ok(userCourse) }
            )
    }

    @PutMapping("/{id}")
    fun updateUserCourse(
        @PathVariable id: UUID,
        @RequestBody dto: UserCourseDto
    ): ResponseEntity<Any> {
        return userCourseService
            .updateUserCourse(id, dto)
            .fold(
                { httpFailureResolver.handleFailure(it) },
                { userCourse -> ResponseEntity.ok(userCourse) }
            )

    }
}