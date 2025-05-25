package com.project.scrumbleserver.service.project

import com.project.scrumbleserver.api.project.ApiGetAllProjectResponse
import com.project.scrumbleserver.api.project.ApiPostProjectRequest
import com.project.scrumbleserver.api.project.ApiPostProjectResponse
import com.project.scrumbleserver.domain.project.Project
import com.project.scrumbleserver.repository.project.ProjectRepository
import com.project.scrumbleserver.service.TagService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val tagService: TagService,
) {
    @Transactional
    fun insert(
        request: ApiPostProjectRequest,
    ): ApiPostProjectResponse {
        val project = projectRepository.save(Project(
            title = request.title,
            description = request.description ?: ""
        ))

        tagService.saveBasicTags(project)

        return ApiPostProjectResponse(project.rowid)
    }

    @Transactional(readOnly = true)
    fun findAll(): ApiGetAllProjectResponse {
        return ApiGetAllProjectResponse(
            projectRepository.findAll().map {
                ApiGetAllProjectResponse.Project(
                    rowid = it.rowid,
                    title = it.title,
                    description = it.description,
                    regDate = it.regDate
                )
            }
        )
    }
}