package com.coursy.courses.dto

import com.coursy.courses.model.ProgressStatus
import java.time.LocalDate
import java.util.*

data class UserCourseDto(
    var id: UUID?,
    var userId: UUID,
    var courseId: UUID,
    var progress: ProgressStatus,
    var finishedDay: LocalDate?,
    var currentContent: UUID
)