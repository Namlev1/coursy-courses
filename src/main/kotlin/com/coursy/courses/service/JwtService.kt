package com.coursy.courses.service

import com.coursy.courses.types.Email
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Service

@Service
class JwtService {
    fun readToken(jwt: PreAuthenticatedAuthenticationToken): Pair<Email, Boolean> {
        val userEmail = jwt.principal as Email
        val isUser = jwt.authorities
            .map { it.authority }
            .contains("ROLE_USER")
        return Pair(userEmail, isUser)
    }
}