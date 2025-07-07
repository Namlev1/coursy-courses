package com.coursy.courses.security

import com.coursy.courses.types.Email
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken


fun PreAuthenticatedAuthenticationToken.readToken(): Pair<Email, Boolean> {
    val userEmail = this.principal as Email
    val isUser = this.authorities
        .map { it.authority }
        .contains("ROLE_USER")
    return Pair(userEmail, isUser)
}