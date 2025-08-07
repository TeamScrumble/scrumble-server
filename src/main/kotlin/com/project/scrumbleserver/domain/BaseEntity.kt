package com.project.scrumbleserver.domain

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @CreatedDate
    @Column(name = "reg_date", updatable = false)
    var regDate: LocalDateTime = LocalDateTime.now()

    @Column(name = "is_deleted", nullable = false)
    private var isDeleted: Boolean = false

    fun delete() {
        isDeleted = true
    }
}
