package com.project.scrumbleserver.infra.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("jwt")
data class JwtProperties(
    val secret: String
) {
    companion object {
        const val ONE_HOUR = 3600L
        const val SEVEN_DAYS = 604800L
    }

    val tokenExpires = ONE_HOUR
}