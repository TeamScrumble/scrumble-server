package com.project.scrumbleserver.domain.projectMember

import com.project.scrumbleserver.domain.BaseEntity
import com.project.scrumbleserver.domain.member.Member
import com.project.scrumbleserver.domain.project.Project
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

@Entity(name = "project_member")
class ProjectMember(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_member_rowid")
    var rowid: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_rowid", nullable = false)
    val project: Project,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_rowid", nullable = false)
    val member: Member,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    var permission: ProjectMemberPermission = ProjectMemberPermission.CAN_VIEW
) : BaseEntity()