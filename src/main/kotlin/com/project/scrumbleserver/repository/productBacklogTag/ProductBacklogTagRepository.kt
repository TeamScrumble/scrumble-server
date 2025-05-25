package com.project.scrumbleserver.repository.productBacklogTag

import com.project.scrumbleserver.domain.productBacklog.ProductBacklog
import com.project.scrumbleserver.domain.productBacklogTag.ProductBacklogTag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductBacklogTagRepository : JpaRepository<ProductBacklogTag, Long> {
    fun findAllByProductBacklog(productBacklog: ProductBacklog): List<ProductBacklogTag>
}