package com.project.scrumbleserver.domain.tag

import com.project.scrumbleserver.domain.BaseEntity
import com.project.scrumbleserver.domain.productBacklog.ProductBacklog
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "product_backlog_tag")
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
