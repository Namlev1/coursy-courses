package com.coursy.courses.failure

sealed class ValidationFailure : Failure {
    data object Empty : ValidationFailure()
    data class TooShort(val minLength: Int) : ValidationFailure()
    data class TooLong(val maxLength: Int) : ValidationFailure()

    override fun message(): String = when (this) {
        Empty -> "Platform name cannot be empty"
        is TooLong -> "Platform name is too long (maximum length: $maxLength)"
        is TooShort -> "Platform name is too short (minimum length: $minLength)"
    }
}
