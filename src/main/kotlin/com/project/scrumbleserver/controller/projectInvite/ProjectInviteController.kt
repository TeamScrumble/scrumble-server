package com.project.scrumbleserver.controller.projectInvite

import com.project.scrumbleserver.api.projectInvite.*
import com.project.scrumbleserver.domain.projectMember.ProjectMemberPermission
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.security.RequestUserRowid
import com.project.scrumbleserver.service.projectInvite.ProjectInviteService
import com.project.scrumbleserver.service.projectMember.ProjectMemberService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Project", description = "프로젝트 초대 관련 API")
class ProjectInviteController(
    private val projectInviteService: ProjectInviteService,
    private val projectMemberService: ProjectMemberService
) {

    @PostMapping(API_POST_PROJECT_INVITE_PATH)
    @Operation(
        summary = "프로젝트 초대",
        description = "요청 정보를 기반으로 회원에게 초대 링크를 전달합니다.",
    )
    fun invite(
        @RequestBody @Valid request: ApiPostProjectInviteRequest,
        @RequestUserRowid userRowid: Long,
    ): ApiResponse<ApiPostProjectInviteResponse> {
        projectMemberService.assertPermission(request.projectRowid, userRowid, ProjectMemberPermission.CAN_EDIT)

        val response = projectInviteService.invite(request.projectRowid, request.email)
        return ApiResponse.of(ApiPostProjectInviteResponse(response))
    }

    @PostMapping(API_POST_PROJECT_INVITE_ACCEPT_PATH)
    @Operation(
        summary = "프로젝트 초대 수락",
        description = "초대 코드를 통해 프로젝트에 참여합니다.",
    )
    fun inviteAccept(
        @PathVariable code: String
    ): ApiResponse<ApiPostProjectInviteAcceptResponse> {
        val response = projectInviteService.inviteAccept(code)
        return ApiResponse.of(ApiPostProjectInviteAcceptResponse(response))
    }
}