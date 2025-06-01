package com.project.scrumbleserver.controller.tag

import com.project.scrumbleserver.api.tag.API_GET_ALL_PROJECT_TAG_PATH
import com.project.scrumbleserver.api.tag.ApiGetAllProjectTagRequest
import com.project.scrumbleserver.api.tag.ApiGetAllProjectTagResponse
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.service.tag.TagService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Tag", description = "Tag 관련 API")
class TagController(
    private val tagService: TagService
) {

    @GetMapping(API_GET_ALL_PROJECT_TAG_PATH)
    @Operation(
        summary = "프로젝트의 태그 목록 조회",
        description = "특정 프로젝트의 모든 태그 목록을 반환합니다.",
    )
    fun findAll(
        @RequestBody @Valid request: ApiGetAllProjectTagRequest,
    ): ApiResponse<ApiGetAllProjectTagResponse> {
        val response = tagService.findAllByProject(request.projectRowid)
        return ApiResponse.of(response)
    }
}