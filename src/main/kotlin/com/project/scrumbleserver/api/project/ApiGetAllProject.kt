package com.project.scrumbleserver.api.project

import java.time.LocalDateTime

object ApiGetAllProject {

    const val PATH = "/api/v1/projects"

    data class Response(
        val projects: List<Project>
    ) {
        data class Project(
            val rowid: Long,
            val title: String,
            val regDate: LocalDateTime,
        )
    }
}