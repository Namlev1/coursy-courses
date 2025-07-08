package com.coursy.courses.failure


sealed class DescriptionFailure : ValidationFailure {
    data object Empty : DescriptionFailure()
    data class TooShort(val minLength: Int) : DescriptionFailure()
    data class TooLong(val maxLength: Int) : DescriptionFailure()

    override fun message(): String = when (this) {
        Empty -> "Description cannot be empty"
        is TooLong -> "Description is too long (maximum length: $maxLength)"
        is TooShort -> "Description is too short (minimum length: $minLength)"
    }
}
