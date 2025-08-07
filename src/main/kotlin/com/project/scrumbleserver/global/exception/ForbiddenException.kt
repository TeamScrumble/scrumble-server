package com.project.scrumbleserver.global.exception

class ForbiddenException(
    override val message: String = "forbidden",
) : Exception(message)
