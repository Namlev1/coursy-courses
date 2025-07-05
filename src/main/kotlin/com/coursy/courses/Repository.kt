package com.coursy.courses

import com.coursy.courses.model.Course
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CourseRepository : JpaRepository<Course, UUID> 