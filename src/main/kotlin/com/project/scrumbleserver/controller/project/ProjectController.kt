package com.project.scrumbleserver.controller.project

import com.project.scrumbleserver.api.project.API_GET_ALL_PROJECT_PATH
import com.project.scrumbleserver.api.project.API_POST_PROJECT_PATH
import com.project.scrumbleserver.api.project.ApiGetAllProjectResponse
import com.project.scrumbleserver.api.project.ApiPostProjectRequest
import com.project.scrumbleserver.api.project.ApiPostProjectResponse
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.security.RequestUserRowid
import com.project.scrumbleserver.service.project.ProjectService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@Tag(name = "Project", description = "프로젝트 관련 API")
class ProjectController(
    private val projectService: ProjectService,
) {
    @PostMapping(API_POST_PROJECT_PATH, consumes = ["multipart/form-data"])
    @Operation(
        summary = "프로젝트 등록",
        description = "요청 정보를 기반으로 새 프로젝트를 생성합니다.",
    )
    fun add(
        @RequestPart("thumbnail", required = false) thumbnail: MultipartFile?,
        @RequestPart("request") @Valid request: ApiPostProjectRequest,
        @RequestUserRowid userRowid: Long,
    ): ApiResponse<ApiPostProjectResponse> {
        val response = projectService.insert(thumbnail, request, userRowid)
        return ApiResponse.of(response)
    }

    @GetMapping(API_GET_ALL_PROJECT_PATH)
    @Operation(
        summary = "전체 프로젝트 조회",
        description = "모든 프로젝트 목록을 반환합니다.",
    )
    fun getAll(): ApiResponse<ApiGetAllProjectResponse> {
        val response = projectService.findAll()
        return ApiResponse.of(response)
    }
}
