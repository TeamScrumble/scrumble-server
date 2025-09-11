package com.project.scrumbleserver.global.api

import com.project.scrumbleserver.global.error.ErrorCode

data class ErrorResponse(
    val code: String,
    val description: String,
) {
    companion object {
        fun from(errorCode: ErrorCode): ErrorResponse {
            return ErrorResponse(
                code = errorCode.code,
                description = errorCode.description,
            )
        }
    }
}
