package com.coursy.courses.service

import com.coursy.courses.dto.CourseCreationRequest
import com.coursy.courses.model.Course
import com.coursy.courses.security.readToken
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthorizationService(
) {
    fun canUpdateCourse(
        course: Course,
        jwt: PreAuthenticatedAuthenticationToken
    ): Boolean {
        val (userEmail, isUser) = jwt.readToken()
        return !isUser || course.email == userEmail.value
    }

    fun canCreateCourse(
        dto: CourseCreationRequest.Validated,
        jwt: PreAuthenticatedAuthenticationToken
    ): Boolean {
        val (userEmail, isUser) = jwt.readToken()
        return !isUser || dto.email.value == userEmail.value
    }
}