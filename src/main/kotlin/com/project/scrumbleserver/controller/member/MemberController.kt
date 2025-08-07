package com.project.scrumbleserver.controller.member

import com.project.scrumbleserver.api.member.API_GET_MEMBER_INFO
import com.project.scrumbleserver.api.member.API_POST_MEMBER_INFO
import com.project.scrumbleserver.api.member.ApiGetMemberInfoResponse
import com.project.scrumbleserver.api.member.ApiPostMemberInfoRequest
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.security.RequestUserRowid
import com.project.scrumbleserver.service.member.MemberService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService,
) {
    @Operation(
        summary = "멤버 추가 정보 입력",
        description = "멤버의 추가 정보를 입력하는 API 입니다.",
    )
    @PostMapping(API_POST_MEMBER_INFO)
    fun enterAdditionalInfo(
        @RequestBody @Valid request: ApiPostMemberInfoRequest,
        @RequestUserRowid userRowid: Long,
    ): ApiResponse<String> {
        memberService.updateInfo(request, userRowid)
        return ApiResponse.empty()
    }

    @Operation(
        summary = "멤버 정보 조회 API",
        description = "요청에 포함된 쿠키로 부터 멤버 정보를 조회하는 API 입니다.",
    )
    @GetMapping(API_GET_MEMBER_INFO)
    fun findByCookie(
        @RequestUserRowid userRowid: Long,
    ): ApiResponse<ApiGetMemberInfoResponse> {
        val response = memberService.findById(userRowid)
        return ApiResponse.of(response)
    }
}
