package com.coursy.courses.dto

import arrow.core.Either
import arrow.core.Nel
import arrow.core.raise.either
import arrow.core.raise.zipOrAccumulate
import com.coursy.courses.failure.ValidationFailure
import com.coursy.courses.model.Course
import com.coursy.courses.types.Description
import com.coursy.courses.types.Name
import java.net.URL
import java.util.*

data class CourseCreationRequest(
    val name: String,
    val description: String,
    val imageURL: URL,
) : SelfValidating<ValidationFailure, CourseCreationRequest.Validated> {

    data class Validated(
        val name: Name,
        val description: Description,
        val imageURL: URL
    ) {
        fun toModel(platformId: UUID) = Course(
            name = name.value,
            description = description.value,
            imageUrl = imageURL.toString(),
            platformId = platformId,
        )
    }

    override fun validate(): Either<Nel<ValidationFailure>, Validated> {
        return either {
            zipOrAccumulate(
                { Description.create(description).bind() },
                { Name.create(name).bind() },
            ) { validDescription, validName ->
                Validated(
                    name = validName,
                    description = validDescription,
                    imageURL = imageURL,
                )
            }
        }
    }
}