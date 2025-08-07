package com.project.scrumbleserver.api.member

const val API_GET_MEMBER_INFO = "/api/v1/member/info"

data class ApiGetMemberInfoResponse(
    val rowid: Long,
    val email: String,
    val nickname: String,
    val profileImageUrl: String? = null,
)
