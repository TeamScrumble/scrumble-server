package com.project.scrumbleserver.api.auth

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

const val API_POST_MEMBER_INFO = "/api/v1/auth/info"

data class ApiPostMemberInfoRequest(
    @field:NotBlank("nickname는 필수로 입력해 주세요.")
    @field:Size(min = 2, max = 15, message = "최소 2자 이상 15자 이하로 입력해 주세요.")
    val nickname: String,
    @field:NotBlank("job은 필수로 입력해 주세요.")
    val job: String,
    val profileImageUrl: String,
)
