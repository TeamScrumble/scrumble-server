package com.project.scrumbleserver.global.exception

class UnauthorizedException(
    override val message: String = "unauthorized",
) : Exception(message)
