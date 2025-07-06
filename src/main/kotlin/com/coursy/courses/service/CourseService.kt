package com.coursy.courses.service

import com.coursy.courses.CourseRepository
import com.coursy.courses.dto.CourseRequest
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class CourseService(val repo: CourseRepository) {
//    fun getAllCourses(): List<CourseResponse> =
//        repo
//            .findAll()
//            .map(Course::toResponse)

    fun saveCourse(
        dto: CourseRequest.Validated,
    ) = repo
        .save(dto.toModel())

//    fun deleteCourse(id: UUID) = repo.deleteById(id)

//    fun deleteCourse(platformId: Long, userEmail: Email): Either<CourseFailure, Unit> {
//        val platform = repo.findByIdOrNull(platformId) ?: return CourseFailure.NotFound(platformId).left()
//        if (platform.userEmail != userEmail.value)
//            return CourseFailure.InvalidEmail(userEmail, platformId).left()
//
//        repo.deleteById(platformId)
//        return Unit.right()
//    }

//    fun getById(id: UUID) =
//        repo
//            .findByIdOrNull(id)?.toResponse()?.right()
//            ?: CourseFailure.NotFound(id).left()

//    fun getByUserEmail(email: Email) =
//        repo.getByUserEmail(email.value)
//            .map(Course::toResponse)
}
