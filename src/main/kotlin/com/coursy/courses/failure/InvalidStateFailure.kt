package com.coursy.courses.failure

class InvalidStateFailure(
    val message: String
) : Failure {
    override fun message(): String = "Invalid state: $message"
}
