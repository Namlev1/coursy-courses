package com.coursy.courses.types

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.coursy.courses.failure.NameFailure

@JvmInline
value class Name private constructor(val value: String) {
    companion object {
        private const val MIN_LENGTH = 2
        private const val MAX_LENGTH = 50

        fun create(value: String): Either<NameFailure, Name> = when {
            value.isEmpty() -> NameFailure.Empty.left()
            value.length < MIN_LENGTH -> NameFailure.TooShort(MIN_LENGTH).left()
            value.length > MAX_LENGTH -> NameFailure.TooLong(MAX_LENGTH).left()
            else -> Name(value).right()
        }
    }
}