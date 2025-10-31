package com.coursy.courses.failure

import java.util.*

sealed class UserCourseFailure : Failure {
    data class NotFound(val id: UUID) : UserCourseFailure()
    data class NotFoundByUserAndVideo(val userId: UUID, val videoId: UUID) : UserCourseFailure()

    override fun message(): String = when (this) {
        is NotFound -> "User course with id=${id} was not found"
        is NotFoundByUserAndVideo -> "User course with userId=${userId} and videoId=${videoId} was not found"
    }
}
