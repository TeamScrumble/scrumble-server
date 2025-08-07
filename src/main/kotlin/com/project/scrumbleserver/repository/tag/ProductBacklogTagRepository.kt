package com.project.scrumbleserver.repository.tag

import com.project.scrumbleserver.domain.tag.ProductBacklogTag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProductBacklogTagRepository : JpaRepository<ProductBacklogTag, Long> {
    @Query(
        """
        SELECT pbt FROM product_backlog_tag pbt
        JOIN FETCH pbt.tag t
        JOIN FETCH t.project
        WHERE pbt.productBacklog.rowid IN :productBacklogRowidList
    """,
    )
    fun findAllByProductBacklogRowidInFetchJoin(
        @Param("productBacklogRowidList") productBacklogRowidList: Set<Long>,
    ): List<ProductBacklogTag>
}
