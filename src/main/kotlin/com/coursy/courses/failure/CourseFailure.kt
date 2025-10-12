package com.coursy.courses.failure

import java.util.*

sealed class CourseFailure : Failure {
    data class NotFound(val id: UUID) : CourseFailure()

    override fun message(): String = when (this) {
        is NotFound -> "Course with id=${id} was not found"
    }
}
