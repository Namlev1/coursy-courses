package com.coursy.courses.service

import com.coursy.courses.CourseRepository
import org.springframework.stereotype.Service

@Service
class CourseService(val repo: CourseRepository) {
//    fun getAllCourses(): List<CourseResponse> =
//        repo
//            .findAll()
//            .map(Course::toResponse)

//    fun saveCourse(
//        dto: CourseRequest.Validated,
//        email: Email
//    ) = repo
//        .save(dto.toModel(email))
//        .right()

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
