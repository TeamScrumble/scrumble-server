package com.project.scrumbleserver.api.projectMember

const val API_GET_PROJECT_MEMBERS = "/api/v1/project-members"

data class ApiGetProjectMembersRequest(
    val projectRowid: Long,
)

data class ApiGetProjectMembersResponse(
    val members: List<Member>,
) {
    data class Member(
        val memberRowid: Long,
        val email: String,
        val profileImageUrl: String?,
        val permission: String,
    )
}
