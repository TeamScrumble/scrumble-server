package com.project.scrumbleserver.api.s3

import jakarta.validation.constraints.NotNull


const val API_S3_PRESIGNED_PUT_GENERATE_PATH = "/api/v1/presigned/put/generate"

data class ApiPresignedPutRequest(
//    @field:NotNull(message = "bucketName은 필수입니다.")
//    val bucketName: String,
    @field:NotNull(message = "objectName은 필수입니다.")
    val objectName: String,
)

data class ApiPresignedPutResponse(
    val url : String,
    val msg : String,
)
