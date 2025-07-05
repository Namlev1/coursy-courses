package com.coursy.courses.types

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.coursy.courses.failure.ValidationFailure

@JvmInline
value class Description private constructor(val value: String) {
    companion object {
        private const val MIN_LENGTH = 2
        private const val MAX_LENGTH = 50

        fun create(value: String): Either<DescriptionFailure, Description> = when {
            value.isEmpty() -> DescriptionFailure.Empty.left()
            value.length < MIN_LENGTH -> DescriptionFailure.TooShort(MIN_LENGTH).left()
            value.length > MAX_LENGTH -> DescriptionFailure.TooLong(MAX_LENGTH).left()
            else -> Description(value).right()
        }
    }
}

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
