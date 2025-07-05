package com.coursy.courses.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/courses")
class CourseController {

//    @GetMapping
//    fun getAllCourses(
//        @RequestParam(defaultValue = "0") page: Int,
//        @RequestParam(defaultValue = "10") size: Int,
//        @RequestParam(required = false) search: String?
//    ): ResponseEntity<Page<CourseDto>> {
//        TODO("Get paginated list of courses for current tenant with optional search")
//    }
//
//    @GetMapping("/{courseId}")
//    fun getCourseById(@PathVariable courseId: Long): ResponseEntity<CourseDetailsDto> {
//        TODO("Get detailed course information by ID")
//    }
//
//    @PostMapping
//    fun createCourse(@RequestBody @Valid createCourseRequest: CreateCourseRequest): ResponseEntity<CourseDto> {
//        TODO("Create new course for current tenant")
//    }
//
//    @PutMapping("/{courseId}")
//    fun updateCourse(
//        @PathVariable courseId: Long,
//        @RequestBody @Valid updateCourseRequest: UpdateCourseRequest
//    ): ResponseEntity<CourseDto> {
//        TODO("Update existing course")
//    }
//
//    @DeleteMapping("/{courseId}")
//    fun deleteCourse(@PathVariable courseId: Long): ResponseEntity<Void> {
//        TODO("Delete course (soft delete)")
//    }
//
//    @PatchMapping("/{courseId}/publish")
//    fun publishCourse(@PathVariable courseId: Long): ResponseEntity<CourseDto> {
//        TODO("Publish course (make it available to students)")
//    }
//
//    @PatchMapping("/{courseId}/unpublish")
//    fun unpublishCourse(@PathVariable courseId: Long): ResponseEntity<CourseDto> {
//        TODO("Unpublish course (hide from students)")
//    }
}