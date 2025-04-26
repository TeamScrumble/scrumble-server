package com.project.scrumbleserver.global.excception

class ServerException(
    override val message: String = "unknown error occurred"
) : Exception(message)