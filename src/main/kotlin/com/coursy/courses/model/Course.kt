package com.coursy.courses.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.Hibernate
import java.util.*

@Entity
class Course(
    @Id
    var id: UUID = UUID.randomUUID(),
    var email: String,
    var name: String,
    var description: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Course

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
} 
