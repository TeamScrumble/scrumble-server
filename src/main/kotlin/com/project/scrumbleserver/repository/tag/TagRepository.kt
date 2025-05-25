package com.project.scrumbleserver.repository.tag

import com.project.scrumbleserver.domain.project.Project
import com.project.scrumbleserver.domain.tag.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<Tag, Long> {
    fun findAllByRowidIsIn(tagRowids: Set<Long>): List<Tag>
    fun findAllByProject(projectId: Project): List<Tag>
}