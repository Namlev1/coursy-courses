package com.coursy.courses.controller

import arrow.core.flatMap
import com.coursy.courses.dto.CourseCreationRequest
import com.coursy.courses.dto.CourseUpdateRequest
import com.coursy.courses.failure.ValidationFailure
import com.coursy.courses.security.AuthenticatedUser
import com.coursy.courses.service.CourseService
import com.coursy.courses.service.ImagesService
import org.springframework.core.io.InputStreamResource
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/api/courses")
class CourseController(
    val courseService: CourseService,
    val httpFailureResolver: HttpFailureResolver,
    val imageService: ImagesService
) {

    @GetMapping("/{courseId}")
    fun getCourse(
        @PathVariable courseId: UUID,
    ): ResponseEntity<Any> {
        return courseService
            .getById(courseId)
            .fold(
                { httpFailureResolver.handleFailure(it) },
                { ResponseEntity.ok(it) }
            )
    }

    @GetMapping("/{courseId}/image")
    fun getLogo(@PathVariable courseId: UUID): ResponseEntity<Any> {
        return imageService
            .getImage(courseId)
            .fold(
                { failure ->
                    ResponseEntity.badRequest()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(failure.message())
                },
                { inputStream ->
                    val resource = InputStreamResource(inputStream)
                    ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource)
                }
            )
    }

    @GetMapping("/page/{platformId}")
    fun getCourseList(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @PathVariable platformId: UUID,
    ): ResponseEntity<Any> =
        when {
            arePageParamsInvalid(page, size) -> ResponseEntity.badRequest().build()
            else -> PageRequest.of(page, size)
                .let { courseService.getPage(it, platformId) }
                .let { ResponseEntity.ok(it) }
        }


    //    @PreAuthorize("hasAnyRole('HOST_OWNER', 'HOST_ADMIN', 'PLATFORM_OWNER', 'PLATFORM_ADMIN')")
    @PostMapping
    fun createCourse(
        @AuthenticationPrincipal principal: AuthenticatedUser,
        @RequestPart("data") courseRequest: CourseCreationRequest,
        @RequestPart("image") image: MultipartFile,
    ): ResponseEntity<Any> {
        return courseRequest
            .validate()
            .mapLeft { failures ->
                if (failures.size == 1) failures.head
                else ValidationFailure.Multiple(failures)
            }
            .flatMap { validatedRequest ->
                courseService.save(validatedRequest, principal)
            }
            .flatMap { course ->
                imageService.uploadImage(image, course.id)
                    .map { course }
            }
            .fold(
                { httpFailureResolver.handleFailure(it) },
                { ResponseEntity.status(HttpStatus.CREATED).body(it) }
            )
    }
    
    @PreAuthorize("hasAnyRole('HOST_OWNER', 'HOST_ADMIN', 'PLATFORM_OWNER', 'PLATFORM_ADMIN')")
    @PutMapping("/{courseId}")
    fun updateCourse(
        @PathVariable courseId: UUID,
        @RequestBody courseRequest: CourseUpdateRequest,
        @AuthenticationPrincipal principal: AuthenticatedUser
    ): ResponseEntity<Any> {
        return courseRequest
            .validate()
            .fold(
                { httpFailureResolver.handleFailure(it) },
                { validatedRequest ->
                    courseService.update(courseId, validatedRequest, principal)
                        .fold(
                            { httpFailureResolver.handleFailure(it) },
                            { course -> ResponseEntity.ok(course) }
                        )
                }
            )
    }

    @PreAuthorize("hasAnyRole('HOST_OWNER', 'HOST_ADMIN', 'PLATFORM_OWNER', 'PLATFORM_ADMIN')")
    @DeleteMapping("/{courseId}")
    fun deleteCourse(
        @PathVariable courseId: UUID,
        @AuthenticationPrincipal principal: AuthenticatedUser
    ): ResponseEntity<Any> {
        return courseService
            .deleteById(courseId, principal)
            .fold(
                { httpFailureResolver.handleFailure(it) },
                { ResponseEntity.status(HttpStatus.NO_CONTENT).build() }
            )
    }

    private fun arePageParamsInvalid(page: Int, size: Int) =
        page < 0 || size <= 0
}