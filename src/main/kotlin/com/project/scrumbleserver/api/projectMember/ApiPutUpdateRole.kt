package com.project.scrumbleserver.api.projectMember

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

const val API_PUT_UPDATE_ROLE_PATH = "/api/v1/project-member/update/role"

data class ApiPutUpdateRoleRequest(
    @field:NotNull(message = "projectRowid는 필수입니다.")
    val projectRowid: Long,
    @field:NotNull(message = "memberRowid는 필수입니다.")
    val memberRowid: Long,
    @field:NotBlank(message = "role은 필수입니다.")
    val role: String,
)

data class ApiPutUpdateRoleResponse(
    val projectMemberRowid: Long,
)