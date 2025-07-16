package com.project.scrumbleserver.api.projectMember

import com.project.scrumbleserver.domain.projectMember.ProjectMemberPermission
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

const val API_PUT_PROJECT_MEMBER_PERMISSION_PATH = "/api/v1/project-member/permission"

data class ApiPutProjectMemberPermissionRequest(
    @field:NotNull(message = "projectRowid는 필수입니다.")
    val projectRowid: Long,
    @field:NotNull(message = "memberRowid는 필수입니다.")
    val memberRowid: Long,
    @field:NotBlank(message = "permission은 필수입니다.")
    val permission: ProjectMemberPermission
)

data class ApiPutProjectMemberPermissionResponse(
    val projectMemberRowid: Long,
)
