package com.project.scrumbleserver.domain.productBacklog

import com.project.scrumbleserver.domain.BaseEntity
import com.project.scrumbleserver.domain.productBacklogTag.ProductBacklogTag
import com.project.scrumbleserver.domain.project.Project
import jakarta.persistence.CascadeType
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
import jakarta.persistence.OneToMany

@Entity(name = "product_backlog")
class ProductBacklog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_backlog_rowid")
    var rowid: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_rowid", nullable = false)
    val project: Project,

    @Column(nullable = false, length = 30)
    var title: String,

    @Column(nullable = false, length = 300)
    var description: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var priority: ProductBacklogPriority = ProductBacklogPriority.NONE,

    @OneToMany(mappedBy = "productBacklog", orphanRemoval = true, cascade = [CascadeType.ALL])
    val tags: MutableList<ProductBacklogTag> = mutableListOf(),
) : BaseEntity()