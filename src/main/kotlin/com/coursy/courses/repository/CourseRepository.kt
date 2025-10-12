package com.coursy.courses.repository

import com.coursy.courses.model.Course
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CourseRepository : JpaRepository<Course, UUID> {
    fun findAllByPlatformId(platformId: UUID, pageable: Pageable): Page<Course>
} 