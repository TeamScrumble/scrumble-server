package com.project.scrumbleserver.domain.productBacklog

import com.project.scrumbleserver.domain.BaseEntity
import com.project.scrumbleserver.domain.project.Project
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "product_backlog")
class ProductBacklog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_backlog_rowid")
    var rowid: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_rowid", nullable = false)
    val project: Project,

    var title: String,

    var description: String,

    // 시작 시간, 예상(완료) 시간인데, 아직은 주석 처리
//    var startDate: LocalDateTime,
//    var endDate: LocalDateTime,
) : BaseEntity()