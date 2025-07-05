package com.coursy.courses.dto

import arrow.core.Either
import arrow.core.Nel
import com.coursy.courses.failure.Failure

interface SelfValidating<E : Failure, V> {
    fun validate(): Either<Nel<E>, V>
}