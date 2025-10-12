package com.coursy.courses.model

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.time.LocalDate
import java.util.*

@Entity
class UserCourse(
    @Id
    var id: UUID? = UUID.randomUUID(),
    var userId: UUID,
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    var course: Course?,
    @Enumerated(EnumType.STRING)
    var progress: ProgressStatus,
    var finishedDay: LocalDate? = null,
    var currentVideo: UUID
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as UserCourse

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
} 
