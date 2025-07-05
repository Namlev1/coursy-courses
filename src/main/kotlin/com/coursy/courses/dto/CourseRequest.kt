package com.coursy.courses.dto

import arrow.core.Either
import arrow.core.Nel
import arrow.core.raise.either
import arrow.core.raise.zipOrAccumulate
import com.coursy.courses.failure.ValidationFailure
import com.coursy.courses.types.Description
import com.coursy.courses.types.Name

data class CourseRequest(
    val name: String,
    val description: String,
) : SelfValidating<ValidationFailure, CourseRequest.Validated> {

    data class Validated(
        val name: Name,
        val description: Description
    )

    override fun validate(): Either<Nel<ValidationFailure>, Validated> {
        return either {
            zipOrAccumulate(
                { Description.create(description).bind() },
                { Name.create(name).bind() },
            ) { validDescription, validName ->
                Validated(
                    name = validName,
                    description = validDescription,
                )
            }
        }
    }
}