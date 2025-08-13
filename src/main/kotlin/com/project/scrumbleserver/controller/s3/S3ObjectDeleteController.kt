package com.project.scrumbleserver.controller.s3

import com.project.scrumbleserver.api.s3.API_S3_DELETE_OBJECT_PATH
import com.project.scrumbleserver.api.s3.ApiS3DeleteObjectRequest
import com.project.scrumbleserver.api.s3.ApiS3DeleteObjectResponse
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.infra.s3.S3Service
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "ObjectDelete", description = "업로드된 객체 삭제 API")
class S3ObjectDeleteController(
    private val s3Service: S3Service
) {
    @PostMapping(API_S3_DELETE_OBJECT_PATH)
    @Operation(
        summary = "객체 삭제",
        description = "지정된 s3 버킷의 객체를 삭제합니다.",
    )
    fun delete(
        @RequestBody @Valid
        request: ApiS3DeleteObjectRequest
    ): ApiResponse<ApiS3DeleteObjectResponse> {
        try {
            s3Service.deleteObject("scrb-asset", request.objectName)
            val response = ApiS3DeleteObjectResponse("${request.objectName} 객체 삭제 성공")
            return ApiResponse.of(response)
        }catch (e: Exception) {
            val response = ApiS3DeleteObjectResponse("${request.objectName} 객체 삭제 실패\n 에러메세지: ${e.message}")
            return ApiResponse.of(response)
        }
    }

}