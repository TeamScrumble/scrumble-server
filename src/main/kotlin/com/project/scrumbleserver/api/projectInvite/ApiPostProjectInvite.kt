package com.project.scrumbleserver.api.projectInvite

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

const val API_POST_PROJECT_INVITE_PATH = "/api/v1/project/invite"

data class ApiPostProjectInviteRequest(
    @field:NotBlank(message = "이메일은 필수입니다.")
    @field:Size(max = 100, message = "이메일은 100자 이하로 입력해야 합니다.")
    val email: String,
    @field:NotNull(message = "projectRowid는 필수입니다.")
    val projectRowid: Long
)

data class ApiPostProjectInviteResponse(
    val projectInviteRowid: Long,
)
