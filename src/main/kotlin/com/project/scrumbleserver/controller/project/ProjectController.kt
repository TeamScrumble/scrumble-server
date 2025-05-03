package com.project.scrumbleserver.controller.project

import com.project.scrumbleserver.api.project.ApiGetAllProject
import com.project.scrumbleserver.api.project.ApiPostProject
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.service.project.ProjectService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProjectController(
    private val projectService: ProjectService
) {

    @PostMapping(ApiPostProject.PATH)
    fun add(
        @RequestBody request: ApiPostProject.Request
    ): ApiResponse<ApiPostProject.Response> {
        val response = projectService.insert(request)
        return ApiResponse.of(response)
    }

    @GetMapping(ApiGetAllProject.PATH)
    fun getAll(): ApiResponse<ApiGetAllProject.Response> {
        val response = projectService.findAll()
        return ApiResponse.of(response)
    }
}