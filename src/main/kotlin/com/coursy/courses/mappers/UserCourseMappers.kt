package com.coursy.courses.mappers

import com.coursy.courses.dto.UserCourseDto
import com.coursy.courses.model.UserCourse
import tech.mappie.api.ObjectMappie
import java.util.*

object UserCourseToDto : ObjectMappie<UserCourse, UserCourseDto>() {
    override fun map(from: UserCourse): UserCourseDto = mapping {
        UserCourseDto::courseId fromExpression { from ->
            from.course?.id ?: throw IllegalStateException("Course cannot be null")
        }
    }
}

object UserCourseDtoToEntity : ObjectMappie<UserCourseDto, UserCourse>() {
    override fun map(from: UserCourseDto): UserCourse = mapping {
        UserCourse::course fromValue null
        UserCourse::id fromExpression { from ->
            from.id ?: UUID.randomUUID()
        }
    }
}
