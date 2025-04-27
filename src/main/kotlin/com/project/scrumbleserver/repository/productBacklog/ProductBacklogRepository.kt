package com.project.scrumbleserver.repository.productBacklog

import com.project.scrumbleserver.domain.productBacklog.ProductBacklog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductBacklogRepository : JpaRepository<ProductBacklog, Long> {
    fun findAllByProjectRowid(productRowid: Long): List<ProductBacklog>
}