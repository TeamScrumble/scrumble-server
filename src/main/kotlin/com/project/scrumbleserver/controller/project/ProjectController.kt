package com.project.scrumbleserver.controller.project

import com.project.scrumbleserver.api.project.ApiGetAllPost
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
        val rowid = projectService.insert(request.title)
        val response = ApiPostProject.Response(rowid)

        return ApiResponse.of(response)
    }

    @GetMapping(ApiGetAllPost.PATH)
    fun getAll(): ApiResponse<List<ApiGetAllPost.Response>> {
        val projects = projectService.findAll()
        val response = projects.map {
            ApiGetAllPost.Response(
                rowid = it.rowid,
                title = it.title,
                regDate = it.regDate
            )
        }

        return ApiResponse.of(response)
    }
}