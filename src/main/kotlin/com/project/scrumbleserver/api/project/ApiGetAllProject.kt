package com.project.scrumbleserver.api.project

import java.time.LocalDateTime

const val API_GET_ALL_PROJECT_PATH = "/api/v1/projects"

data class ApiGetAllProjectResponse(
    val projects: List<Project>
) {
    data class Project(
        val rowid: Long,
        val title: String,
        val description: String,
        val thumbnailUrl: String,
        val regDate: LocalDateTime,
    )
}
