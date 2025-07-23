package com.project.scrumbleserver.controller.productBacklog

import com.project.scrumbleserver.api.productBacklog.API_GET_ALL_PRODUCT_BACKLOG_PATH
import com.project.scrumbleserver.api.productBacklog.API_POST_PRODUCT_BACKLOG_PATH
import com.project.scrumbleserver.api.productBacklog.ApiGetAllProductBacklogRequest
import com.project.scrumbleserver.api.productBacklog.ApiGetAllProductBacklogResponse
import com.project.scrumbleserver.api.productBacklog.ApiPostProductBacklogRequest
import com.project.scrumbleserver.api.productBacklog.ApiPostProductBacklogResponse
import com.project.scrumbleserver.domain.projectMember.ProjectMemberPermission
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.security.RequestUserRowid
import com.project.scrumbleserver.service.productBacklog.ProductBacklogService
import com.project.scrumbleserver.service.projectMember.ProjectMemberService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "ProductBacklog", description = "프로덕트 백로그 관련 API")
class ProductBacklogController(
    private val productBacklogService: ProductBacklogService,
    private val projectMemberService: ProjectMemberService
) {

    @PostMapping(API_POST_PRODUCT_BACKLOG_PATH)
    @Operation(
        summary = "프로덕트 백로그 등록",
        description = "요청 정보를 기반으로 새 프로덕트 백로그를 생성합니다."
    )
    fun add(
        @RequestBody @Valid request: ApiPostProductBacklogRequest,
        @RequestUserRowid userRowid: Long,
    ): ApiResponse<ApiPostProductBacklogResponse> {
        projectMemberService.assertPermission(request.projectRowid, userRowid, ProjectMemberPermission.CAN_EDIT)

        val productBacklogRowid = productBacklogService.insert(request)
        val response = ApiPostProductBacklogResponse(productBacklogRowid)
        return ApiResponse.of(response)
    }

    @GetMapping(API_GET_ALL_PRODUCT_BACKLOG_PATH)
    @Operation(
        summary = "전체 프로덕트 백로그 조회",
        description = "특정 프로젝트의 모든 백로그 목록을 반환합니다."
    )
    fun findAll(
        @RequestBody
        request: ApiGetAllProductBacklogRequest
    ): ApiResponse<List<ApiGetAllProductBacklogResponse>> {
        val productBacklogs = productBacklogService.findAll(request.projectRowid)
        return ApiResponse.of(productBacklogs)
    }
}