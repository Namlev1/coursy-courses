package com.coursy.courses.dto

import com.coursy.courses.model.Course
import java.util.*


data class CourseResponse(
    val id: UUID,
    val name: String,
    val description: String
)

fun Course.toResponse(): CourseResponse =
    CourseResponse(
        id = this.id,
        name = this.name,
        description = this.description
    )
