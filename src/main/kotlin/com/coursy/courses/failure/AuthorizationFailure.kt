package com.coursy.courses.failure

sealed class AuthorizationFailure : Failure {
    data object UnauthorizedAccess : AuthorizationFailure()

    override fun message(): String = when (this) {
        is UnauthorizedAccess -> "User does not have access to this resource"
    }
}
