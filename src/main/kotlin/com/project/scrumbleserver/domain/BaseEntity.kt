package com.project.scrumbleserver.domain

import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

abstract class BaseEntity {

    @CreatedDate
    var regDate: LocalDateTime = LocalDateTime.MIN

    private var isDeleted: Boolean = false

    fun delete() {
        isDeleted = true
    }
}