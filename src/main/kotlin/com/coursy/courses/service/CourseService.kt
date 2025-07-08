package com.coursy.courses.service

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.coursy.courses.CourseRepository
import com.coursy.courses.dto.CourseCreationRequest
import com.coursy.courses.dto.CourseResponse
import com.coursy.courses.dto.CourseUpdateRequest
import com.coursy.courses.dto.toResponse
import com.coursy.courses.failure.AuthorizationFailure
import com.coursy.courses.failure.CourseFailure
import com.coursy.courses.failure.Failure
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class CourseService(
    val repo: CourseRepository,
    val authorizationService: AuthorizationService,
    private val pagedResourcesAssembler: PagedResourcesAssembler<CourseResponse>
) {
    fun save(
        dto: CourseCreationRequest.Validated,
        jwt: PreAuthenticatedAuthenticationToken
    ): Either<Failure, CourseResponse> {
        if (!authorizationService.canCreateCourse(dto, jwt))
            return AuthorizationFailure.UnauthorizedAccess.left()

        return repo
            .save(dto.toModel())
            .toResponse()
            .right()
    }

    fun deleteById(
        id: UUID,
        jwt: PreAuthenticatedAuthenticationToken
    ): Either<Failure, Unit> {
        val course = repo.findByIdOrNull(id)
            ?: return CourseFailure.NotFound(id).left()

        if (!authorizationService.canUpdateCourse(course, jwt))
            return AuthorizationFailure.UnauthorizedAccess.left()

        repo.deleteById(id)
        return Unit.right()
    }

    fun getById(
        id: UUID,
        jwt: PreAuthenticatedAuthenticationToken
    ): Either<Failure, CourseResponse> {
        val course = repo.findByIdOrNull(id)
            ?: return CourseFailure.NotFound(id).left()

        if (!authorizationService.canUpdateCourse(course, jwt))
            return AuthorizationFailure.UnauthorizedAccess.left()

        return course.toResponse().right()
    }

    fun update(
        id: UUID,
        dto: CourseUpdateRequest.Validated,
        jwt: PreAuthenticatedAuthenticationToken,
    ): Either<Failure, CourseResponse> {
        val course = repo.findByIdOrNull(id)
            ?: return CourseFailure.NotFound(id).left()

        if (!authorizationService.canUpdateCourse(course, jwt))
            return AuthorizationFailure.UnauthorizedAccess.left()

        course.apply {
            dto.name?.let { name = it.value }
            dto.description?.let { description = it.value }
        }

        return repo
            .save(course)
            .toResponse()
            .right()
    }


    fun getPage(pageRequest: PageRequest) =
        repo.findAll(pageRequest)
            .map { it.toResponse() }
            .let { pagedResourcesAssembler.toModel(it) }
}
