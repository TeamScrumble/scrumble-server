package com.project.scrumbleserver.domain.productBacklogTag

import com.project.scrumbleserver.domain.BaseEntity
import com.project.scrumbleserver.domain.productBacklog.ProductBacklog
import com.project.scrumbleserver.domain.productBacklog.ProductBacklogPriority
import com.project.scrumbleserver.domain.project.Project
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

@Entity
class ProductBacklogTag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_backlog_tag_rowid")
    var rowid: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_backlog_rowid", nullable = false)
    val productBacklog: ProductBacklog,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_rowid", nullable = false)
    val tag: Tag,
) : BaseEntity()