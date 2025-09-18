package com.coursy.courses.security

import com.coursy.courses.types.Email
import java.util.*


data class AuthenticatedUser(
    val email: Email,
    val id: UUID,
    val platformId: UUID?
)
