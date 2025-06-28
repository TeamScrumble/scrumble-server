package com.project.scrumbleserver.domain.projectInviteLink

import com.project.scrumbleserver.domain.BaseEntity
import com.project.scrumbleserver.domain.member.Member
import com.project.scrumbleserver.domain.productBacklog.ProductBacklog
import com.project.scrumbleserver.domain.productBacklog.ProductBacklogPriority
import com.project.scrumbleserver.domain.project.Project
import com.project.scrumbleserver.domain.projectMember.ProductMemberPermission
import com.project.scrumbleserver.domain.tag.Tag
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "project_invite_link")
class ProjectInviteLink(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_invite_link_rowid")
    var rowid: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_rowid", nullable = false)
    val project: Project,

    @Column(nullable = true, length = 100)
    var uuid: String,
) : BaseEntity()