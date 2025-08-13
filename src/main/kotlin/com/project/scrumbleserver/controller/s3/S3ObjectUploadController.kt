package com.project.scrumbleserver.controller.s3

import com.project.scrumbleserver.api.s3.API_S3_UPLOAD_OBJECT_PATH
import com.project.scrumbleserver.api.s3.ApiS3UploadObjectRequest
import com.project.scrumbleserver.api.s3.ApiS3UploadObjectResponse
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
@Tag(name = "ObjectUpload", description = "파일 업로드 API")
class S3ObjectUploadController(
    private val s3Service: S3Service
) {
    @PostMapping(API_S3_UPLOAD_OBJECT_PATH)
    @Operation(
        summary = "파일 업로드",
        description = "지정된 s3 버킷에 파일을 업로드합니다.",
    )
    fun upload(
        @RequestBody @Valid
        request: ApiS3UploadObjectRequest
    ): ApiResponse<ApiS3UploadObjectResponse>{
        println("here")
        val contentLength = request.file.size
        val inputStream = request.file.inputStream

        val future = s3Service.uploadObject(
            bucketName = "scrb-asset",
            objectName = request.file.originalFilename ?: "unknown",
            inputStream = inputStream,
            contentType = request.file.contentType ?: "application/octet-stream",
            contentLength = contentLength
        )

        return try {
            val response = future.get() // ExecutionException으로 래핑됨
            ApiResponse.of(
                ApiS3UploadObjectResponse(
                    "업로드 완료: ${response?.eTag()}",
                    request.objectName
                )
            )
        } catch (e: ExecutionException) {
            val actualException = e.cause ?: e
            ApiResponse.of(
                ApiS3UploadObjectResponse(
                    "업로드 실패: ${actualException.message}",
                    ""
                )
            )
        } catch (e: Exception) {
            ApiResponse.of(
                ApiS3UploadObjectResponse(
                    "업로드 실패: ${e.message}",
                    ""
                )
            )
        }
    }
}