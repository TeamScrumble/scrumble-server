package com.project.scrumbleserver.api.s3

import jakarta.validation.constraints.NotNull
import java.io.InputStream


const val API_S3_DOWNLOAD_OBJECT_PATH = "/api/v1/download/object"

data class ApiS3DownloadObjectRequest(
//    @field:NotNull(message = "bucketName은 필수입니다.")
//    val bucketName: String,
    @field:NotNull(message = "objectName은 필수입니다.")
    val objectName: String,
)

data class ApiS3DownloadObjectResponse(
    val file : InputStream?=null,
    val fileName : String?=null,
    val msg : String,
)
