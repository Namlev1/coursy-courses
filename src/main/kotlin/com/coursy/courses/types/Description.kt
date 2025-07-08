package com.coursy.courses.types

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.coursy.courses.failure.DescriptionFailure

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
