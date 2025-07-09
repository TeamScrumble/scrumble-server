package com.project.scrumbleserver.api.auth

const val API_POST_MEMBER_INFO = "/api/v1/auth/info"

data class ApiPostMemberInfoRequest(
    val nickname: String,
    val job: String,
    val profileImageUrl: String,
)
