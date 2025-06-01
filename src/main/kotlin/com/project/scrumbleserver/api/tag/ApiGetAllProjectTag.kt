package com.project.scrumbleserver.api.tag

import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

const val API_GET_ALL_PROJECT_TAG_PATH = "/api/v1/project/tags"

data class ApiGetAllProjectTagRequest(
    @field:NotNull(message = "projectRowid는 필수입니다.")
    val projectRowid: Long,
)

data class ApiGetAllProjectTagResponse(
    val tags: List<Tag>
) {
    data class Tag(
        val tagRowid: Long,
        val projectRowid: Long,
        val title: String,
        val color: String,
    )
}
