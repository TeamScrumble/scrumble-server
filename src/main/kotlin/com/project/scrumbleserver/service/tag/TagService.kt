package com.project.scrumbleserver.service.tag

import com.project.scrumbleserver.api.tag.ApiGetAllProjectTagResponse
import com.project.scrumbleserver.domain.project.Project
import com.project.scrumbleserver.domain.tag.BasicTag
import com.project.scrumbleserver.domain.tag.Tag
import com.project.scrumbleserver.global.exception.BusinessException
import com.project.scrumbleserver.repository.project.ProjectRepository
import com.project.scrumbleserver.repository.tag.TagRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagService(
    private val projectRepository: ProjectRepository,
    private val tagRepository: TagRepository,
) {
    @Transactional
    fun saveBasicTags(project: Project) {
        val tags =
            BasicTag.entries.map {
                Tag(
                    project = project,
                    title = it.title,
                    color = it.color,
                )
            }

        tagRepository.saveAll(tags)
    }

    @Transactional(readOnly = true)
    fun findAllByProject(projectRowid: Long): ApiGetAllProjectTagResponse {
        val project = projectRepository.findByIdOrNull(projectRowid) ?: throw BusinessException("프로젝트를 찾을 수 없습니다.")

        val projectTags = tagRepository.findAllByProject(project)

        return ApiGetAllProjectTagResponse(
            projectTags.map {
                ApiGetAllProjectTagResponse.Tag(
                    tagRowid = it.rowid,
                    projectRowid = it.project.rowid,
                    title = it.title,
                    color = it.color,
                )
            },
        )
    }
}
