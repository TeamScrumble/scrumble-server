package com.project.scrumbleserver.service.project

import com.project.scrumbleserver.domain.project.Project
import com.project.scrumbleserver.repository.project.ProjectRepository
import org.springframework.stereotype.Repository

@Repository
class ProjectService(
    private val projectRepository: ProjectRepository
) {
    fun insert(title: String): Long {
        val project = Project(title = title)
        projectRepository.save(project)
        return project.rowid
    }

    fun findAll(): List<Project> {
        return projectRepository.findAll()
    }
}