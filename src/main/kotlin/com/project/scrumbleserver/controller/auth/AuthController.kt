package com.project.scrumbleserver.controller.auth

import com.project.scrumbleserver.api.auth.API_POST_MEMBER_INFO
import com.project.scrumbleserver.api.auth.ApiPostMemberInfoRequest
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.security.RequestUserRowid
import com.project.scrumbleserver.service.auth.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping(API_POST_MEMBER_INFO)
    fun enterAdditionalInfo(
        @RequestBody request: ApiPostMemberInfoRequest,
        @RequestUserRowid userRowid: Long,
    ): ApiResponse<String> {
        authService.enterAdditionalInfo(request, userRowid)
        return ApiResponse.empty()
    }
}
