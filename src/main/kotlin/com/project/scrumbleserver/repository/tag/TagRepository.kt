package com.project.scrumbleserver.repository.tag

import com.project.scrumbleserver.domain.project.Project
import com.project.scrumbleserver.domain.tag.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<Tag, Long> {
    @Query(
        """
        SELECT t FROM tag t 
        JOIN FETCH t.project 
        WHERE t.rowid IN :ids 
        AND t.project.rowid = :projectRowid
    """,
    )
    fun findAllByIdWithProject(
        @Param("ids") ids: Set<Long>,
        @Param("projectRowid") projectRowid: Long,
    ): List<Tag>

    fun findAllByProject(project: Project): List<Tag>
}
