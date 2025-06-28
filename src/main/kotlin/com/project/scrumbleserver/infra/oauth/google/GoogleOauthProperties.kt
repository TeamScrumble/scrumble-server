package com.project.scrumbleserver.infra.oauth.google

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("oauth.google")
data class GoogleOauthProperties(
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String,
    val scope: String,
)
