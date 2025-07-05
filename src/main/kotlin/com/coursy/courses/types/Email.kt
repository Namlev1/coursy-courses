package com.coursy.courses.types

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.coursy.courses.failure.ValidationFailure

@JvmInline
value class Email private constructor(val value: String) {
    companion object {
        private val EMAIL_REGEX = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        private const val MIN_LENGTH = 6
        private const val MAX_LENGTH = 60

        fun create(value: String): Either<EmailFailure, Email> = when {
            value.isEmpty() -> EmailFailure.Empty.left()
            "@" !in value -> EmailFailure.MissingAtSymbol.left()
            value.length < MIN_LENGTH -> EmailFailure.TooShort(MIN_LENGTH).left()
            value.length > MAX_LENGTH -> EmailFailure.TooLong(MIN_LENGTH).left()
            !value.matches(EMAIL_REGEX) -> EmailFailure.InvalidFormat.left()
            else -> Email(value).right()
        }
    }
}

sealed class EmailFailure : ValidationFailure {
    data object Empty : EmailFailure()
    data object MissingAtSymbol : EmailFailure()
    data object InvalidFormat : EmailFailure()
    data class TooShort(val minLength: Int) : EmailFailure()
    data class TooLong(val maxLength: Int) : EmailFailure()

    override fun message(): String = when (this) {
        Empty -> "Email cannot be empty"
        MissingAtSymbol -> "Email must contain an @ symbol"
        InvalidFormat -> "Email format is invalid"
        is TooLong -> "Email is too long (maximum length: $maxLength)"
        is TooShort -> "Email is too short (minimum length: $minLength)"
    }
}
