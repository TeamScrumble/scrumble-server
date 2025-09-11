package com.project.scrumbleserver.controller.s3

import com.project.scrumbleserver.api.s3.API_S3_PRESIGNED_GET_URL_PATH
import com.project.scrumbleserver.api.s3.ApiPresignedGetRequest
import com.project.scrumbleserver.api.s3.ApiPresignedGetResponse
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.infra.s3.S3Service
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "PresignedGetUrl", description = "파일 다운로드 링크를 제공하는 API")
class S3PresignedGetUrlController(
    private val s3Service: S3Service
) {
    @PostMapping(API_S3_PRESIGNED_GET_URL_PATH)
    @Operation(
        summary = "파일 다운로드 링크 제공",
        description = "지정된 s3 버킷에 정해진 기간동안 파일을 다운을 수 있는 링크를 전달합니다.",
    )
    fun getPresignedUrl(
        @RequestBody @Valid
        request: ApiPresignedGetRequest
    ): ApiResponse<ApiPresignedGetResponse> {
        return try {
            val url = s3Service.generatePresignedGetUrl("scrb-asset", request.objectName)
            ApiResponse.of(ApiPresignedGetResponse("Presigned URL 생성 성공", url))
        } catch (e: software.amazon.awssdk.services.s3.model.NoSuchBucketException) {
            ApiResponse.of(ApiPresignedGetResponse("버킷이 존재하지 않습니다: ${e.message}", ""))
        } catch (e: software.amazon.awssdk.services.s3.model.NoSuchKeyException) {
            ApiResponse.of(ApiPresignedGetResponse("파일이 존재하지 않습니다: ${e.message}", ""))
        } catch (e: software.amazon.awssdk.services.s3.model.S3Exception) {
            val errorMessage = e.awsErrorDetails()?.errorMessage() ?: e.message
            ApiResponse.of(ApiPresignedGetResponse("S3 오류: $errorMessage", ""))
        } catch (e: Exception) {
            ApiResponse.of(ApiPresignedGetResponse("Presigned URL 생성 실패: ${e.message}", ""))
        }
    }
}