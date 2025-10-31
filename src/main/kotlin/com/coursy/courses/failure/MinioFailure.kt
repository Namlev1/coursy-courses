package com.coursy.courses.failure

class MinIoFailure(val exception: String?) : Failure {
    override fun message() = "Unexpected MinIo exception: $exception"
}
