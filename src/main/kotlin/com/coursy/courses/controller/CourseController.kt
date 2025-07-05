package com.coursy.courses.controller

import com.auth0.jwt.JWT
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/courses")
class CourseController {
    @GetMapping("/public")
    fun publicEndpoint() = "hello from public endpoint"

    @GetMapping("/test")
    fun authenticatedEndpoint(authentication: Authentication): String {
        val userEmail = authentication.principal as String
        val token = authentication.credentials as String
        val jwt = JWT.decode(token)
        val roles = authentication.authorities

        return "email: $userEmail\ntoken: $token\njwt: $jwt\nroles: $roles"
    }

    // todo fix
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    fun authorizedEndpoint(): String {
        return "You've passed authorization flow!"
    }
}