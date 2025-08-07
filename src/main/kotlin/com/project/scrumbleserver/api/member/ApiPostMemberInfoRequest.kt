package com.project.scrumbleserver.api.member

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

const val API_POST_MEMBER_INFO = "/api/v1/member/info"

data class ApiPostMemberInfoRequest(
    @field:NotBlank("nickname는 필수로 입력해 주세요.")
    @field:Size(min = 2, max = 15, message = "최소 2자 이상 15자 이하로 입력해 주세요.")
    val nickname: String,
    val profileImageUrl: String,
)
