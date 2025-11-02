package com.coursy.courses.service

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.coursy.courses.dto.UserCourseDto
import com.coursy.courses.failure.CourseFailure
import com.coursy.courses.failure.Failure
import com.coursy.courses.failure.UserCourseFailure
import com.coursy.courses.mappers.UserCourseDtoToEntity
import com.coursy.courses.mappers.UserCourseToDto
import com.coursy.courses.repository.CourseRepository
import com.coursy.courses.repository.UserCourseRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class UserCourseService(
    val userCourseRepository: UserCourseRepository,
    val courseRepository: CourseRepository,
) {
    fun getByUserId(userId: UUID) =
        userCourseRepository
            .getUserCoursesByUserId(userId)
            .map { UserCourseToDto.map(it) }

    fun getByUserIdAndCourseId(userId: UUID, videoId: UUID): Either<UserCourseFailure, UserCourseDto> {
        val userCourse = userCourseRepository
            .getByUserIdAndCourseId(userId, videoId)
            ?: return UserCourseFailure.NotFoundByUserAndVideo(userId, videoId).left()
        return UserCourseToDto.map(userCourse).right()
    }

    fun getByUserCourseId(id: UUID): Either<UserCourseFailure, UserCourseDto> {
        val userCourse = userCourseRepository.findByIdOrNull(id)
            ?: return UserCourseFailure.NotFound(id).left()

        return UserCourseToDto.map(userCourse).right()
    }

    fun createUserCourse(dto: UserCourseDto): Either<Failure, UserCourseDto> {
        val course = courseRepository.findByIdOrNull(dto.courseId)
            ?: return CourseFailure.NotFound(dto.courseId).left()

        val userCourse = UserCourseDtoToEntity.map(dto)
        userCourse.course = course

        val saved = userCourseRepository.save(userCourse)
        return UserCourseToDto.map(saved).right()
    }

    fun updateUserCourse(id: UUID, dto: UserCourseDto): Either<UserCourseFailure, UserCourseDto> {
        val userCourse = userCourseRepository.findByIdOrNull(id)
            ?: return UserCourseFailure.NotFound(id).left()

        userCourse.apply {
            progress = dto.progress
            finishedDay = dto.finishedDay
            currentContent = dto.currentContent
        }

        return UserCourseToDto.map(userCourse).right()
    }
}