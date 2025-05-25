package com.project.scrumbleserver.api.tag

import java.time.LocalDateTime

const val API_GET_ALL_TAG_PATH = "/api/v1/tags"

data class ApiGetAllTagRequest(
    val projectRowid: Long
)

data class ApiGetAllTagResponse(
    val tags: List<Tag>
) {
    data class Tag(
        val tagRowid: Long,
        val projectRowid: Long,
        val title: String,
        val color: String,
        val regDate: LocalDateTime
    )
}
