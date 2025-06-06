package com.project.scrumbleserver.controller.auth

import com.project.scrumbleserver.api.auth.API_GOOGLE_LOGIN_PATH
import com.project.scrumbleserver.api.auth.API_GOOGLE_OAUTH_CALLBACK_PATH
import com.project.scrumbleserver.infra.oauth.google.GoogleOauthProperties
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@RestController
class GoogleLoginController(
    private val googleOauthProperties: GoogleOauthProperties,
) {
    @GetMapping(API_GOOGLE_LOGIN_PATH)
    fun googleLogin(
        @RequestParam state: String,
    ): ResponseEntity<Unit> {
        println(state)
        val googleAuthUrl = UriComponentsBuilder
            .fromHttpUrl("https://accounts.google.com/o/oauth2/auth")
            .queryParam("client_id", googleOauthProperties.clientId)
            .queryParam("redirect_uri", googleOauthProperties.redirectUri)
            .queryParam("response_type", "code")
            .queryParam("scope", googleOauthProperties.scope)
            .queryParam("state", state)
            .build()
            .toUriString()

        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(googleAuthUrl))
            .build()
    }

    @GetMapping(API_GOOGLE_OAUTH_CALLBACK_PATH)
    fun googleLoginCallback(
        @RequestParam code: String,
        @RequestParam state: String,
        httpResponse: HttpServletResponse,
    ): ResponseEntity<Unit> {
        Cookie("access_token", "access-token").apply {
            path = "/"
            isHttpOnly = true
            secure = true
            maxAge = 3600
        }.also { cookie ->
            httpResponse.addCookie(cookie)
        }

        val redirectUri = URLDecoder.decode(state, StandardCharsets.UTF_8)
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(redirectUri))
            .build()
    }
}
