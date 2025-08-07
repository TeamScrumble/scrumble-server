package com.project.scrumbleserver.domain.assignee

import com.project.scrumbleserver.domain.BaseEntity
import com.project.scrumbleserver.domain.member.Member
import com.project.scrumbleserver.domain.productBacklog.ProductBacklog
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "assignee")
class Assignee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignee_rowid")
    var rowid: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_rowid", nullable = false)
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_backlog_rowid", nullable = false)
    val productBacklog: ProductBacklog
) : BaseEntity()