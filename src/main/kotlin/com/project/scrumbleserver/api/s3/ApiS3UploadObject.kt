package com.project.scrumbleserver.api.s3

import com.project.scrumbleserver.domain.projectMember.ProjectMemberPermission
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.multipart.MultipartFile


const val API_S3_UPLOAD_OBJECT_PATH = "/api/v1/upload/object"

data class ApiS3UploadObjectRequest(
//    @field:NotNull(message = "bucketName은 필수입니다.")
//    val bucketName: String,
    @field:NotNull(message = "objectName은 필수입니다.")
    val objectName: String,
    @field:NotNull(message = "file 첨부는 필수입니다.")
    val file: MultipartFile,
)

data class ApiS3UploadObjectResponse(
    val msg: String,
    val objectName: String,
)
