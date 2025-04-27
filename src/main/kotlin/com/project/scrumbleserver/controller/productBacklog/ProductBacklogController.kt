package com.project.scrumbleserver.controller.productBacklog

import com.project.scrumbleserver.api.productBacklog.ApiPostProductBacklog
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.service.productBacklog.ProductBacklogService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductBacklogController(
    private val productBacklogService: ProductBacklogService,
) {

    @PostMapping(ApiPostProductBacklog.PATH)
    fun add(
        @RequestBody request: ApiPostProductBacklog.Request
    ): ApiResponse<ApiPostProductBacklog.Response> {
        val productBacklogRowid = productBacklogService.insert(request)
        val response = ApiPostProductBacklog.Response(productBacklogRowid)

        return ApiResponse.of(response)
    }
}