package com.project.scrumbleserver.global.api

import org.springframework.http.HttpStatus

data class ApiResponse<T>(
    val status: HttpStatus,
    val data: T,
) {
    companion object {
        fun <T> of(
            data: T,
            status: HttpStatus = HttpStatus.OK,
        ): ApiResponse<T> = ApiResponse(status, data)

        fun <T> of(status: HttpStatus = HttpStatus.OK): ApiResponse<String> = ApiResponse(HttpStatus.OK, status.name)

        fun empty(): ApiResponse<String> = ApiResponse(HttpStatus.OK, "Api Call Success")
    }
}
