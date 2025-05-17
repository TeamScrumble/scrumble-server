package com.project.scrumbleserver.api.project

import java.time.LocalDateTime

const val ApiGetAllProjectPath = "/api/v1/projects"

data class ApiGetAllProjectResponse(
    val projects: List<Project>
) {
    data class Project(
        val rowid: Long,
        val title: String,
        val description: String,
        val regDate: LocalDateTime,
    )
}
