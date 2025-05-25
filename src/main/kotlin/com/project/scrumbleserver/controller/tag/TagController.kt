package com.project.scrumbleserver.controller.tag

import com.project.scrumbleserver.api.tag.API_GET_ALL_TAG_PATH
import com.project.scrumbleserver.api.tag.ApiGetAllTagRequest
import com.project.scrumbleserver.api.tag.ApiGetAllTagResponse
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.service.tag.TagService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Tag", description = "프로젝트 태그 관련 API")
class TagController(
    private val tagService: TagService,
) {
    @GetMapping(API_GET_ALL_TAG_PATH)
    @Operation(
        summary = "프로젝트의 전체 태그 조회",
        description = "프로젝트의 모든 태그 목록을 반환합니다."
    )
    fun findAll(
        @RequestBody
        request: ApiGetAllTagRequest
    ): ApiResponse<List<ApiGetAllTagResponse>> {
        val tags = tagService.findAllByProjectRowid(request.projectRowid)

        return ApiResponse.of(tags)
    }
}