package com.coursy.courses.failure

import arrow.core.Nel

interface ValidationFailure : Failure {
    data class Multiple(val failures: Nel<ValidationFailure>) : ValidationFailure {
        override fun message(): String =
            "Multiple validation errors: ${failures.map { it.message() }.joinToString(", ")}"
    }

//    data object Empty : ValidationFailure()
//    data class TooShort(val minLength: Int) : ValidationFailure()
//    data class TooLong(val maxLength: Int) : ValidationFailure()
//    data object MissingAtSymbol : ValidationFailure()
//    data object InvalidFormat : ValidationFailure()
//
//    override fun message(): String = when (this) {
//        Empty -> "Platform name cannot be empty"
//        is TooLong -> "Platform name is too long (maximum length: $maxLength)"
//        is TooShort -> "Platform name is too short (minimum length: $minLength)"
//        MissingAtSymbol -> "Email must contain an @ symbol"
//        InvalidFormat -> "Email format is invalid"
//        is Multiple -> "Multiple validation errors: ${failures.map { it.message() }.joinToString(", ")}"
//    }
}