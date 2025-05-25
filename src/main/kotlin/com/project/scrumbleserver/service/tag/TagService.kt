package com.project.scrumbleserver.service.tag

import com.project.scrumbleserver.api.tag.ApiGetAllTagResponse
import com.project.scrumbleserver.domain.project.Project
import com.project.scrumbleserver.domain.tag.BasicTag
import com.project.scrumbleserver.domain.tag.Tag
import com.project.scrumbleserver.repository.project.ProjectRepository
import com.project.scrumbleserver.repository.tag.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class TagService(
    private val projectRepository: ProjectRepository,
    private val tagRepository: TagRepository
) {
    @Transactional
    fun insert(
        project: Project,
    ) {
        val tagList = BasicTag.entries.map { tag ->
            Tag(
                project = project,
                title = tag.title,
                color = tag.color
            )
        }
        tagRepository.saveAll(tagList)
    }

    @Transactional(readOnly = true)
    fun findAllByProjectRowid(
        projectRowid: Long,
    ): List<ApiGetAllTagResponse> {
        val project = projectRepository.findById(projectRowid)
            .getOrNull() ?: throw IllegalArgumentException("프로젝트를 찾을 수 없습니다. ID: $projectRowid")

        val tags = tagRepository.findAllByProject(project)

        return tags.map {
            ApiGetAllTagResponse.Tag(
                tagRowid = it.rowid,
                projectRowid = it.project.rowid,
                title = it.title,
                color = it.color,
                regDate = it.regDate
            )
        }.let { tagList ->
            return listOf(ApiGetAllTagResponse(tagList))
        }
    }
}