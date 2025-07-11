package com.project.scrumbleserver.domain.projectInvite

import com.project.scrumbleserver.domain.BaseEntity
import com.project.scrumbleserver.domain.member.Member
import com.project.scrumbleserver.domain.project.Project
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.util.UUID
import jakarta.persistence.PrePersist
import java.time.LocalDateTime

@Entity(name = "project_invite_link")
class ProjectInvite(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_invite_link_rowid")
    var rowid: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_rowid", nullable = false)
    val project: Project,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_rowid", nullable = false)
    val member: Member,

    @Column(length = 100, nullable = false)
    var uuid: String = "",

    @Column(name = "expired_at", nullable = false, updatable = false)
    var expiredAt: LocalDateTime = LocalDateTime.now().plusDays(1),
) : BaseEntity() {
    @PrePersist
    fun generateUuid() {
        if (uuid.isBlank()) {
            uuid = UUID.randomUUID().toString().substring(0, 20)
        }
    }
}