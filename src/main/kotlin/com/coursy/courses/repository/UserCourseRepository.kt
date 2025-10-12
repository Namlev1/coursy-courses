package com.coursy.courses.repository

import com.coursy.courses.model.UserCourse
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserCourseRepository : JpaRepository<UserCourse, UUID> {
    fun getUserCoursesByUserId(userId: UUID): MutableList<UserCourse>
}