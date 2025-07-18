package com.project.scrumbleserver.controller.member

import com.project.scrumbleserver.api.member.API_POST_MEMBER_INFO
import com.project.scrumbleserver.api.member.ApiPostMemberInfoRequest
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.security.RequestUserRowid
import com.project.scrumbleserver.service.member.MemberService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService
) {

    @Operation(
        summary = "멤버 추가 정보 입력",
        description = "멤버의 추가 정보를 입력하는 API 입니다."
    )
    @PostMapping(API_POST_MEMBER_INFO)
    fun enterAdditionalInfo(
        @RequestBody @Valid request: ApiPostMemberInfoRequest,
        @RequestUserRowid userRowid: Long,
    ): ApiResponse<String> {
        memberService.enterAdditionalInfo(request, userRowid)
        return ApiResponse.empty()
    }
}
