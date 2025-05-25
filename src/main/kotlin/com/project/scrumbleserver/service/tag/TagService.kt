package com.project.scrumbleserver.service.tag

import com.project.scrumbleserver.domain.project.Project
import com.project.scrumbleserver.domain.tag.BasicTag
import com.project.scrumbleserver.domain.tag.Tag
import com.project.scrumbleserver.repository.tag.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagService(
    private val tagRepository: TagRepository
) {
    @Transactional
    fun insert(
        project: Project,
    ) {
        BasicTag.entries.map { tag ->
            tagRepository.save(
                Tag(
                    project = project,
                    title = tag.title,
                    color = tag.color
                )
            )
        }
    }
}