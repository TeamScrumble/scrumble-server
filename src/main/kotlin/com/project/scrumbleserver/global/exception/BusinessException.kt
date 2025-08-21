package com.project.scrumbleserver.global.exception

import com.project.scrumbleserver.global.error.ErrorCode
import org.springframework.http.HttpStatus

class BusinessException(
    errorCode: ErrorCode,
    val status: HttpStatus = HttpStatus.BAD_REQUEST
) : RuntimeException(errorCode.code) {
    override val message: String = errorCode.code
}
