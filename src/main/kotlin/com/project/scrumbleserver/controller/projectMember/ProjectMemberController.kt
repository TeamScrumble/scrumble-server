package com.project.scrumbleserver.controller.projectMember

import com.project.scrumbleserver.api.projectMember.API_GET_PROJECT_MEMBERS
import com.project.scrumbleserver.api.projectMember.API_PUT_PROJECT_MEMBER_PERMISSION_PATH
import com.project.scrumbleserver.api.projectMember.ApiGetProjectMembersRequest
import com.project.scrumbleserver.api.projectMember.ApiGetProjectMembersResponse
import com.project.scrumbleserver.api.projectMember.ApiPutProjectMemberPermissionRequest
import com.project.scrumbleserver.api.projectMember.ApiPutProjectMemberPermissionResponse
import com.project.scrumbleserver.domain.projectMember.ProjectMemberPermission
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.security.RequestUserRowid
import com.project.scrumbleserver.service.projectMember.ProjectMemberService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "ProjectMember", description = "프로젝트 회원 관련 API")
class ProjectMemberController(
    private val projectMemberService: ProjectMemberService,
) {
    @GetMapping(API_GET_PROJECT_MEMBERS)
    @Operation(
        summary = "프로젝트 회원 조회",
        description = "특정 프로젝트에 속한 모든 회원의 정보를 반환합니다.",
    )
    fun findAllProjectMembers(
        @RequestBody request: ApiGetProjectMembersRequest,
    ): ApiResponse<ApiGetProjectMembersResponse> {
        val projectMembers = projectMemberService.findAllByProjectRowid(request.projectRowid)
        return ApiResponse.of(projectMembers)
    }

    @PutMapping(API_PUT_PROJECT_MEMBER_PERMISSION_PATH)
    @Operation(
        summary = "프로젝트 회원 권한 수정",
        description = "프로젝트에 속한 회원의 권한을 수정합니다.",
    )
    fun edit(
        @RequestBody @Valid
        request: ApiPutProjectMemberPermissionRequest,
        @RequestUserRowid userRowid: Long,
    ): ApiResponse<ApiPutProjectMemberPermissionResponse> {
        projectMemberService.assertPermission(request.projectRowid, userRowid, ProjectMemberPermission.OWNER)

        val projectMemberRowid = projectMemberService.edit(request.projectRowid, request.memberRowid, request.permission, userRowid)
        val response = ApiPutProjectMemberPermissionResponse(projectMemberRowid)
        return ApiResponse.of(response)
    }
}
