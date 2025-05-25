package com.project.scrumbleserver.service

import com.project.scrumbleserver.domain.project.Project
import com.project.scrumbleserver.domain.tag.BasicTag
import com.project.scrumbleserver.domain.tag.Tag
import com.project.scrumbleserver.repository.tag.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagService(
    private val tagRepository: TagRepository,
) {
    @Transactional
    fun saveBasicTags(project: Project) {
        val tags = BasicTag.entries.map {
            Tag(
                project = project,
                title = it.title,
                color = it.color,
            )
        }

        tagRepository.saveAll(tags)
    }
}