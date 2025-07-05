package com.coursy.courses.security

import arrow.core.Option
import com.auth0.jwt.JWT
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        extractToken(request).map { token ->
            val jwt = JWT.decode(token)
            val email = jwt.subject
            val roles = jwt.getClaim("roles")?.asList(String::class.java) ?: emptyList()
            val authorities = roles.map { SimpleGrantedAuthority("ROLE_$it") }

            val authentication = PreAuthenticatedAuthenticationToken(
                email,
                token,
                authorities
            )
            authentication.isAuthenticated = true
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }

    private fun extractToken(request: HttpServletRequest): Option<String> {
        return Option.fromNullable(request.getHeader("Authorization"))
            .filter { it.startsWith("Bearer ") }
            .map { it.substring(7) }
    }
}