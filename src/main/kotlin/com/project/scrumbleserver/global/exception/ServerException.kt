package com.project.scrumbleserver.global.exception

class ServerException(
    override val message: String = "unknown error occurred"
) : RuntimeException(message)