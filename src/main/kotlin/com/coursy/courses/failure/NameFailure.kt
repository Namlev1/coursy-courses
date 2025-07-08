package com.coursy.courses.failure


sealed class NameFailure : ValidationFailure {
    data object Empty : NameFailure()
    data class TooShort(val minLength: Int) : NameFailure()
    data class TooLong(val maxLength: Int) : NameFailure()

    override fun message(): String = when (this) {
        Empty -> "Course name cannot be empty"
        is TooLong -> "Course name is too long (maximum length: $maxLength)"
        is TooShort -> "Course name is too short (minimum length: $minLength)"
    }
}
