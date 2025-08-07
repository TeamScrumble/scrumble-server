package com.project.scrumbleserver.global.exception

import org.springframework.http.HttpStatus

class BusinessException(
    override val message: String,
    val status: HttpStatus = HttpStatus.BAD_REQUEST,
) : RuntimeException(message)
