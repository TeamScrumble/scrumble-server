package com.project.scrumbleserver.controller.s3

import com.project.scrumbleserver.api.s3.API_S3_DOWNLOAD_OBJECT_PATH
import com.project.scrumbleserver.api.s3.ApiS3DownloadObjectRequest
import com.project.scrumbleserver.api.s3.ApiS3DownloadObjectResponse
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.infra.s3.S3Service
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ExecutionException

@RestController
@Tag(name = "ObjectDownloadUrl", description = "파일 다운로드를 제공하는 API")
class S3ObjectDownloadController(
    private val s3Service: S3Service
) {
    @PostMapping(API_S3_DOWNLOAD_OBJECT_PATH)
    @Operation(
        summary = "파일 다운로드",
        description = "지정된 s3 버킷에 파일을 다운받습니다.",
    )
    fun download(
        @RequestBody @Valid
        request: ApiS3DownloadObjectRequest
    ): ApiResponse<ApiS3DownloadObjectResponse> {
        val future = s3Service.downloadObject("scrb-asset", request.objectName)

        return try {
            val file = future.get() // ExecutionException으로 래핑됨
            ApiResponse.of(
                ApiS3DownloadObjectResponse(
                    file,
                    request.objectName,
                    "파일 다운로드 성공"
                )
            )
        } catch (e: ExecutionException) {
            val actualException = e.cause ?: e
            ApiResponse.of(
                ApiS3DownloadObjectResponse(
                    null,
                    null,
                    "파일 다운로드 실패: ${actualException.message}"
                )
            )
        } catch (e: Exception) {
            ApiResponse.of(
                ApiS3DownloadObjectResponse(
                    null,
                    null,
                    "파일 다운로드 실패: ${e.message}"
                )
            )
        }
    }

}