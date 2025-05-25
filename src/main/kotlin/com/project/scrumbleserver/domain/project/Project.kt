package com.project.scrumbleserver.domain.project

import com.project.scrumbleserver.domain.BaseEntity
import com.project.scrumbleserver.domain.productBacklog.ProductBacklog
import com.project.scrumbleserver.domain.tag.Tag
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

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

    @OneToMany(mappedBy = "project", cascade = [CascadeType.ALL], orphanRemoval = true)
    val tags: MutableList<Tag> = mutableListOf()
) : BaseEntity()
