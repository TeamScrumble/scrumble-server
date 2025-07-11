package com.project.scrumbleserver.controller.projectInvite

import com.project.scrumbleserver.api.project.API_GET_ALL_PROJECT_PATH
import com.project.scrumbleserver.api.project.API_POST_PROJECT_PATH
import com.project.scrumbleserver.api.project.ApiGetAllProjectResponse
import com.project.scrumbleserver.api.project.ApiPostProjectRequest
import com.project.scrumbleserver.api.project.ApiPostProjectResponse
import com.project.scrumbleserver.api.projectInvite.API_POST_PROJECT_INVITE_PATH
import com.project.scrumbleserver.api.projectInvite.ApiPostProjectInviteRequest
import com.project.scrumbleserver.api.projectInvite.ApiPostProjectInviteResponse
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.service.project.ProjectService
import com.project.scrumbleserver.service.projectInvite.ProjectInviteService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@Tag(name = "Project", description = "프로젝트 초대 관련 API")
class ProjectInviteController(
    private val projectInviteService: ProjectInviteService
) {

    @PostMapping(API_POST_PROJECT_INVITE_PATH)
    @Operation(
        summary = "프로젝트 초대",
        description = "요청 정보를 기반으로 회원에게 초대 링크를 전달합니다.",
    )
    fun invite(
        @RequestBody @Valid
        request: ApiPostProjectInviteRequest,
    ): ApiResponse<ApiPostProjectInviteResponse> {
        val response = projectInviteService.invite(request.projectRowid, request.email)
        return ApiResponse.of(ApiPostProjectInviteResponse(response))
    }
}