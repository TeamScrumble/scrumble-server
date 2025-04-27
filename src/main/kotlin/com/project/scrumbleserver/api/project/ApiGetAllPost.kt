package com.project.scrumbleserver.api.project

import java.time.LocalDateTime

object ApiGetAllPost {

    const val PATH = "/api/v1/project"

    class Response(
        val rowid: Long,
        val title: String,
        val regDate: LocalDateTime,
    )
}