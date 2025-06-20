package com.project.scrumbleserver.controller.s3Test

import com.project.scrumbleserver.api.project.API_GET_ALL_PROJECT_PATH
import com.project.scrumbleserver.api.project.API_POST_PROJECT_PATH
import com.project.scrumbleserver.api.project.ApiGetAllProjectResponse
import com.project.scrumbleserver.api.project.ApiPostProjectRequest
import com.project.scrumbleserver.api.project.ApiPostProjectResponse
import com.project.scrumbleserver.global.api.ApiResponse
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
@Tag(name = "S3Test", description = "S3 Test API")
class S3TestController(
    private val projectService: ProjectService,
) {


}