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
import com.coursy.courses.failure.InvalidStateFailure
import com.coursy.courses.model.Course
import com.coursy.courses.security.AuthenticatedUser
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class CourseService(
    val repo: CourseRepository,
    private val pagedResourcesAssembler: PagedResourcesAssembler<CourseResponse>
) {
    fun save(
        dto: CourseCreationRequest.Validated,
        principal: AuthenticatedUser
    ): Either<Failure, CourseResponse> {
        val platformId = principal.platformId
            ?: return InvalidStateFailure("User has no platformId").left()

        return repo
            .save(dto.toModel(platformId))
            .toResponse()
            .right()
    }

    fun deleteById(
        id: UUID,
        principal: AuthenticatedUser
    ): Either<Failure, Unit> {
        val course = repo.findByIdOrNull(id)
            ?: return CourseFailure.NotFound(id).left()

        val platformId = principal.platformId

        if (!canAccess(platformId, course)) {
            return AuthorizationFailure.UnauthorizedAccess.left()
        }

        repo.deleteById(id)
        return Unit.right()
    }

    fun getById(
        id: UUID
    ): Either<Failure, CourseResponse> {
        val course = repo.findByIdOrNull(id)
            ?: return CourseFailure.NotFound(id).left()

        return course.toResponse().right()
    }

    fun update(
        id: UUID,
        dto: CourseUpdateRequest.Validated,
        principal: AuthenticatedUser
    ): Either<Failure, CourseResponse> {
        val course = repo.findByIdOrNull(id)
            ?: return CourseFailure.NotFound(id).left()

        val platformId = principal.platformId
        if (!canAccess(platformId, course)) {
            return AuthorizationFailure.UnauthorizedAccess.left()
        }

        course.apply {
            dto.name?.let { name = it.value }
            dto.description?.let { description = it.value }
        }

        return repo
            .save(course)
            .toResponse()
            .right()
    }

    fun getPage(pageRequest: PageRequest, platformId: UUID) =
        repo.findAllByPlatformId(platformId, pageRequest)
            .map { it.toResponse() }
            .let { pagedResourcesAssembler.toModel(it) }

    private fun canAccess(platformId: UUID?, course: Course): Boolean =
        platformId == null || course.platformId != platformId
}
