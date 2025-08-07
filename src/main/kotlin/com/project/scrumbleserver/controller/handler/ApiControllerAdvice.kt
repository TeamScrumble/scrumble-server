package com.project.scrumbleserver.controller.handler

import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.global.api.ErrorResponse
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
    fun exceptionHandler(
        e: BusinessException,
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        val errorResponse = ApiResponse.of(ErrorResponse(e.message), e.status)
        logger.warn(e.message)
        return ResponseEntity<ApiResponse<ErrorResponse>>(errorResponse, e.status)
    }

    @ExceptionHandler(ServerException::class)
    fun exceptionHandler(
        e: ServerException,
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        val errorResponse = ApiResponse.of(ErrorResponse(e.message), HttpStatus.INTERNAL_SERVER_ERROR)
        logger.error(e.stackTraceToString())
        return ResponseEntity<ApiResponse<ErrorResponse>>(errorResponse, errorResponse.status)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun exceptionHandler(
        e: MethodArgumentNotValidException,
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        val errorMessages = e.bindingResult.fieldErrors.associate {
            it.field to (it.defaultMessage ?: "잘못된 값입니다.")
        }
        logger.warn(e.message)
        val errorResponse = ApiResponse.of(
            ErrorResponse(errorMessages.toString()),
            HttpStatus.BAD_REQUEST
        )

        return ResponseEntity<ApiResponse<ErrorResponse>>(errorResponse, errorResponse.status)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun exceptionHandler(
        e: HttpMessageNotReadableException,
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        val errorResponse = ApiResponse.of(
            ErrorResponse(e.message ?: "잘못된 요청 형식입니다."),
            HttpStatus.BAD_REQUEST
        )
        logger.warn(e.message)
        return ResponseEntity<ApiResponse<ErrorResponse>>(errorResponse, errorResponse.status)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun exceptionHandler(
        e: MissingServletRequestParameterException,
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        val errorResponse = ApiResponse.of(
            ErrorResponse(e.message ?: "필수 요청 파라미터가 누락되었습니다."),
            HttpStatus.BAD_REQUEST
        )
        logger.warn(e.message)
        return ResponseEntity<ApiResponse<ErrorResponse>>(errorResponse, errorResponse.status)
    }

    @ExceptionHandler(Exception::class)
    fun exceptionHandler(
        e: Exception,
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        val errorResponse = ApiResponse.of(
            ErrorResponse("서버 내부에서 에러가 발생했습니다."),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
        logger.error(e.stackTraceToString())
        return ResponseEntity<ApiResponse<ErrorResponse>>(errorResponse, errorResponse.status)
    }
}
