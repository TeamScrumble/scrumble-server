package com.project.scrumbleserver.global.exception

import com.project.scrumbleserver.global.error.InternalServerError

class ServerException(
    message: String? = null,
) : BaseException(InternalServerError, message)
