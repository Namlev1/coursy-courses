package com.coursy.courses.service

import com.coursy.courses.dto.CourseCreationRequest
import com.coursy.courses.model.Course
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthorizationService(
    val jwtService: JwtService
) {
    fun canUpdateCourse(
        course: Course,
        jwt: PreAuthenticatedAuthenticationToken
    ): Boolean {
        val (userEmail, isUser) = jwtService.readToken(jwt)
        return !isUser || course.email == userEmail.value
    }

    fun canCreateCourse(
        dto: CourseCreationRequest.Validated,
        jwt: PreAuthenticatedAuthenticationToken
    ): Boolean {
        val (userEmail, isUser) = jwtService.readToken(jwt)
        return !isUser || dto.email.value == userEmail.value
    }
}