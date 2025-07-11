package com.project.scrumbleserver.domain.project

import com.project.scrumbleserver.domain.BaseEntity
import com.project.scrumbleserver.domain.member.Member
import jakarta.persistence.*

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_rowid", nullable = false)
    val owner: Member,
) : BaseEntity()
