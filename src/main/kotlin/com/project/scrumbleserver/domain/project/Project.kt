package com.project.scrumbleserver.domain.project

import com.project.scrumbleserver.domain.BaseEntity
import jakarta.persistence.*
import java.util.UUID

@Entity
class Project(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_rowid")
    var rowid: Long = 0L,

    @Column(nullable = false, length = 30)
    var title: String,

    @Column(nullable = true, length = 150)
    var description: String,

    @Column(nullable = false, length = 1000)
    var thumbnail: String,

    @Column(nullable = true, length = 100)
    var uuid: String = "",
) : BaseEntity() {
    @PrePersist
    fun generateUuid() {
        if (uuid.isBlank()) {
            uuid = UUID.randomUUID().toString().substring(0, 20)
        }
    }
}
