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

        fun create(value: String): Either<ValidationFailure, Email> = when {
            value.isEmpty() -> ValidationFailure.Empty.left()
            "@" !in value -> ValidationFailure.MissingAtSymbol.left()
            value.length < MIN_LENGTH -> ValidationFailure.TooShort(MIN_LENGTH).left()
            value.length > MAX_LENGTH -> ValidationFailure.TooLong(MIN_LENGTH).left()
            !value.matches(EMAIL_REGEX) -> ValidationFailure.InvalidFormat.left()
            else -> Email(value).right()
        }
    }
}
