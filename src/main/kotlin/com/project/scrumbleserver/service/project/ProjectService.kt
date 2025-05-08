package com.project.scrumbleserver.service.project

import com.project.scrumbleserver.api.project.ApiGetAllProject
import com.project.scrumbleserver.api.project.ApiPostProject
import com.project.scrumbleserver.domain.project.Project
import com.project.scrumbleserver.repository.project.ProjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
) {
    @Transactional
    fun insert(
        request: ApiPostProject.Request
    ): ApiPostProject.Response {
        val project = Project(title = request.title)
        projectRepository.save(project)
        return ApiPostProject.Response(project.rowid)
    }

    @Transactional(readOnly = true)
    fun findAll(): ApiGetAllProject.Response {
        return ApiGetAllProject.Response(
            projectRepository.findAll().map {
                ApiGetAllProject.Response.Project(
                    rowid = it.rowid,
                    title = it.title,
                    regDate = it.regDate
                )
            }
        )
    }
}