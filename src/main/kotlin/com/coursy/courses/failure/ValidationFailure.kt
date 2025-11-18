package com.coursy.courses.failure

import arrow.core.Nel

interface ValidationFailure : Failure {
    data class Multiple(val failures: Nel<ValidationFailure>) : ValidationFailure {
        override fun message(): String =
            "Multiple validation errors: ${failures.map { it.message() }.joinToString(", ")}"
    }

}