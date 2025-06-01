package com.project.scrumbleserver.domain.tag

import com.project.scrumbleserver.domain.BaseEntity
import com.project.scrumbleserver.domain.productBacklog.ProductBacklogPriority
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

@Entity(name = "tag")
class Tag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_rowid")
    var rowid: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_rowid", nullable = false)
    val project: Project,

    @Column(nullable = false, length = 100)
    var title: String,

    @Column(nullable = false, length = 10)
    var color: String
) : BaseEntity()