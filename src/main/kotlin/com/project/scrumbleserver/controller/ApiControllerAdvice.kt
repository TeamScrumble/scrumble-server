package com.project.scrumbleserver.controller

import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.global.excception.BusinessException
import com.project.scrumbleserver.global.excception.ServerException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class ApiControllerAdvice {

    data class ErrorResponse(
        val message: String
    )

    @ExceptionHandler(BusinessException::class)
    fun exceptionHandler(e: BusinessException): ResponseEntity<ApiResponse<ErrorResponse>> {
        val errorResponse = ApiResponse.of(ErrorResponse(e.message), e.status)
        logger.warn(e.message)
        return ResponseEntity<ApiResponse<ErrorResponse>>(errorResponse, e.status)
    }

    @ExceptionHandler(ServerException::class)
    fun exceptionHandler(e: ServerException): ResponseEntity<ApiResponse<ErrorResponse>> {
        val errorResponse = ApiResponse.of(ErrorResponse(e.message), HttpStatus.INTERNAL_SERVER_ERROR)
        logger.error(e.stackTraceToString())
        return ResponseEntity<ApiResponse<ErrorResponse>>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(Exception::class)
    fun exceptionHandler(e: Exception): ResponseEntity<ApiResponse<ErrorResponse>> {
        val errorResponse = ApiResponse.of(ErrorResponse(e.message ?: "error"), HttpStatus.INTERNAL_SERVER_ERROR)
        logger.error(e.stackTraceToString())
        return ResponseEntity<ApiResponse<ErrorResponse>>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
