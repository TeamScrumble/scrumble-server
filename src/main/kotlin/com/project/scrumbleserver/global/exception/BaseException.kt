package com.project.scrumbleserver.global.exception

import com.project.scrumbleserver.global.error.ErrorCode

abstract class BaseException(
    val code: ErrorCode,
    message: String? = null
) : RuntimeException(message ?: code.description)