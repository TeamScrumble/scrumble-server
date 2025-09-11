package com.project.scrumbleserver.controller.handler

import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.global.api.ErrorResponse
import com.project.scrumbleserver.global.error.CommonError
import com.project.scrumbleserver.global.error.InternalServerError
import com.project.scrumbleserver.global.exception.BusinessException
import com.project.scrumbleserver.global.exception.ServerException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class ApiControllerAdvice {
    @ExceptionHandler(BusinessException::class)
    fun exceptionHandler(e: BusinessException): ResponseEntity<ApiResponse<ErrorResponse>> {
        val errorResponse = ApiResponse.of(ErrorResponse.from(e.code), e.status)
        logger.warn(e.message)
        return ResponseEntity<ApiResponse<ErrorResponse>>(errorResponse, e.status)
    }

    @ExceptionHandler(ServerException::class)
    fun exceptionHandler(e: ServerException): ResponseEntity<ApiResponse<ErrorResponse>> {
        val errorResponse = ApiResponse.of(ErrorResponse.from(e.code), HttpStatus.INTERNAL_SERVER_ERROR)
        logger.error(e.stackTraceToString())
        return ResponseEntity<ApiResponse<ErrorResponse>>(errorResponse, errorResponse.status)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleInvalidArgs(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<ErrorResponse>> {
        val errorMessages = e.bindingResult.fieldErrors.associate {
            it.field to (it.defaultMessage ?: CommonError.INVALID_PARAMETER.description) // Changed
        }
        logger.warn(e.message)
        val errorResponse = ApiResponse.of(
            ErrorResponse(CommonError.INVALID_PARAMETER.code, errorMessages.toString()),
            HttpStatus.BAD_REQUEST
        )
        return ResponseEntity(errorResponse, errorResponse.status)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleInvalidFormat(e: HttpMessageNotReadableException): ResponseEntity<ApiResponse<ErrorResponse>> {
        val errorResponse = ApiResponse.of(
            ErrorResponse.from(CommonError.INVALID_FORMAT),
            HttpStatus.BAD_REQUEST
        )
        logger.warn(e.message)
        return ResponseEntity(errorResponse, errorResponse.status)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParam(e: MissingServletRequestParameterException): ResponseEntity<ApiResponse<ErrorResponse>> {
        val errorResponse = ApiResponse.of(
            ErrorResponse.from(CommonError.MISSING_PARAMETER),
            HttpStatus.BAD_REQUEST,
        )
        logger.warn(e.message)
        return ResponseEntity(errorResponse, errorResponse.status)
    }

    @ExceptionHandler(Exception::class)
    fun exceptionHandler(e: Exception): ResponseEntity<ApiResponse<ErrorResponse>> {
        val errorResponse =
            ApiResponse.of(
                ErrorResponse.from(InternalServerError),
                HttpStatus.INTERNAL_SERVER_ERROR,
            )
        logger.error(e.stackTraceToString())
        return ResponseEntity<ApiResponse<ErrorResponse>>(errorResponse, errorResponse.status)
    }
}
