package com.coursy.courses.service

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.coursy.courses.CourseRepository
import com.coursy.courses.dto.CourseRequest
import com.coursy.courses.dto.CourseResponse
import com.coursy.courses.dto.toResponse
import com.coursy.courses.failure.AuthorizationFailure
import com.coursy.courses.failure.CourseFailure
import com.coursy.courses.failure.Failure
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class CourseService(
    val repo: CourseRepository,
    val authorizationService: AuthorizationService
) {
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

    fun getById(
        id: UUID,
        jwt: PreAuthenticatedAuthenticationToken
    ): Either<Failure, CourseResponse> {
        val course = repo.findByIdOrNull(id)
            ?: return CourseFailure.NotFound(id).left()

        if (!authorizationService.canAccessCourse(course, jwt))
            return AuthorizationFailure.UnauthorizedAccess.left()

        return course.toResponse().right()
    }

//    fun getByUserEmail(email: Email) =
//        repo.getByUserEmail(email.value)
//            .map(Course::toResponse)

}
