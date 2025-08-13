package com.project.scrumbleserver.controller.s3

import com.project.scrumbleserver.api.s3.API_S3_PRESIGNED_PUT_GENERATE_PATH
import com.project.scrumbleserver.api.s3.ApiPresignedPutRequest
import com.project.scrumbleserver.api.s3.ApiPresignedPutResponse
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.infra.s3.S3Service
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import software.amazon.awssdk.services.s3.model.NoSuchBucketException
import software.amazon.awssdk.services.s3.model.S3Exception

@RestController
@Tag(name = "PresignedPutUrl", description = "파일 업로드 링크를 제공하는 API")
class S3PresignedPutUrlController(
    private val s3Service: S3Service
) {
    @PostMapping(API_S3_PRESIGNED_PUT_GENERATE_PATH)
    @Operation(
        summary = "파일 업로드 링크 제공",
        description = "지정된 s3 버킷에 정해진 기간동안 파일을 업로드할 수 있는 링크를 전달합니다.",
    )
    fun putPresignedUrl(
        @RequestBody @Valid
        request: ApiPresignedPutRequest
    ): ApiResponse<ApiPresignedPutResponse> {
        return try {
            val url = s3Service.generatePresignedPutUrl("scrb-asset", request.objectName)
            ApiResponse.of(ApiPresignedPutResponse(url,"Presigned URL 생성 성공"))
        } catch (e: NoSuchBucketException) {
            ApiResponse.of(ApiPresignedPutResponse("","버킷이 존재하지 않습니다: ${e.message}"))
        } catch (e: S3Exception) {
            val errorMessage = e.awsErrorDetails()?.errorMessage() ?: e.message
            ApiResponse.of(ApiPresignedPutResponse("","S3 오류: $errorMessage"))
        } catch (e: Exception) {
            ApiResponse.of(ApiPresignedPutResponse("","Presigned URL 생성 실패: ${e.message}"))
        }
    }
}