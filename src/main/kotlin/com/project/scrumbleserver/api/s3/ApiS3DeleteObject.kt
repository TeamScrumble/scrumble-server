package com.project.scrumbleserver.api.s3

import com.project.scrumbleserver.domain.projectMember.ProjectMemberPermission
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull


const val API_S3_DELETE_OBJECT_PATH = "/api/v1/delete/object"

data class ApiS3DeleteObjectRequest(
//    @field:NotNull(message = "bucketName은 필수입니다.")
//    val bucketName: String,
    @field:NotNull(message = "objectName은 필수입니다.")
    val objectName: String,
)

data class ApiS3DeleteObjectResponse(
    val msg: String,
)
