package com.coursy.courses.service

import com.coursy.courses.model.Course
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthorizationService(
    val jwtService: JwtService
) {
    fun canAccessCourse(
        course: Course,
        jwt: PreAuthenticatedAuthenticationToken
    ): Boolean {
        val (userEmail, isUser) = jwtService.readToken(jwt)
        return !isUser || course.email != userEmail.value
    }
}