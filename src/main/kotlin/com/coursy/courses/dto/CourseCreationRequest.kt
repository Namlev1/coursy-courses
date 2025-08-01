package com.coursy.courses.dto

import arrow.core.Either
import arrow.core.Nel
import arrow.core.raise.either
import arrow.core.raise.zipOrAccumulate
import com.coursy.courses.failure.ValidationFailure
import com.coursy.courses.model.Course
import com.coursy.courses.types.Description
import com.coursy.courses.types.Email
import com.coursy.courses.types.Name

data class CourseCreationRequest(
    val name: String,
    val description: String,
    val email: String
) : SelfValidating<ValidationFailure, CourseCreationRequest.Validated> {

    data class Validated(
        val name: Name,
        val description: Description,
        val email: Email
    ) {
        fun toModel() = Course(
            name = this.name.value,
            description = this.description.value,
            email = this.email.value
        )
    }

    override fun validate(): Either<Nel<ValidationFailure>, Validated> {
        return either {
            zipOrAccumulate(
                { Description.create(description).bind() },
                { Name.create(name).bind() },
                { Email.create(email).bind() }
            ) { validDescription, validName, validEmail ->
                Validated(
                    name = validName,
                    description = validDescription,
                    email = validEmail
                )
            }
        }
    }
}