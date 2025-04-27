package com.project.scrumbleserver.api.project

object ApiPostProject {
    const val PATH = "/api/v1/project"

    data class Request(
        val title: String
    )

    data class Response(
        val rowid: Long
    )
}