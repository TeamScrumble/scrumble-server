package com.project.scrumbleserver.api.project

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

const val API_POST_PROJECT_PATH = "/api/v1/project"

data class ApiPostProjectRequest(
    @field:NotBlank(message = "프로젝트 title은 필수입니다.")
    @field:Size(max = 30, message = "프로젝트 title은 30자 이하로 입력해야 합니다.")
    val title: String,
    @field:Size(max = 150, message = "프로젝트 description은 150자 이하로 입력해야 합니다.")
    val description: String?,
)

data class ApiPostProjectResponse(
    val projectRowid: Long,
)
