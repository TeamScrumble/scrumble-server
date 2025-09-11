package com.project.scrumbleserver.api.s3

import jakarta.validation.constraints.NotNull


const val API_S3_PRESIGNED_GET_URL_PATH = "/api/v1/presigned/get/generate"

data class ApiPresignedGetRequest(
    @field:NotNull(message = "objectName은 필수입니다.")
    val objectName: String,
)

data class ApiPresignedGetResponse(
    val msg : String,
    val url: String,
)
