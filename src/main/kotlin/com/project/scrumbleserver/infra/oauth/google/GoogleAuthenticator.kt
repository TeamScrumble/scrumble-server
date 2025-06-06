package com.project.scrumbleserver.infra.oauth.google

import com.fasterxml.jackson.annotation.JsonProperty
import com.project.scrumbleserver.global.excception.BusinessException
import com.project.scrumbleserver.global.excception.ServerException
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import java.net.URI

@Component
class GoogleAuthenticator(
    private val restTemplate: RestTemplate,
    private val googleOauthProperties: GoogleOauthProperties,
) {
    companion object {
        private const val GOOGLE_AUTH_API_ENDPOINT = "https://oauth2.googleapis.com/token"
        private const val GOOGLE_USER_API_ENDPOINT = "https://www.googleapis.com/oauth2/v2/userinfo"
    }

    private data class AuthResponse(
        @JsonProperty("access_token")
        val accessToken: String,
    )

    fun authenticate(code: String): String {
        val accessToken = callAuthApi(code)
        return callUserInfoApi(accessToken)
    }

    private fun callAuthApi(code: String): String {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
            charset("UTF-8")
        }

        val body = LinkedMultiValueMap<String, String>().apply {
            add("code", code)
            add("client_id", googleOauthProperties.clientId)
            add("client_secret", googleOauthProperties.clientSecret)
            add("redirect_uri", googleOauthProperties.redirectUri)
            add("grant_type", "authorization_code")
        }

        val request = HttpEntity(body, headers)
        val response = restTemplate.exchange(
            GOOGLE_AUTH_API_ENDPOINT,
            HttpMethod.POST,
            request,
            AuthResponse::class.java
        )

        if (response.statusCode.isError) {
            throw BusinessException("구글 인증 요청에 실패했습니다.")
        }

        return response.body?.accessToken ?: throw ServerException("구글로 부터 사용자 인증 정보를 가져오는데 실패했습니다.")
    }

    data class GoogleUserInfo(
        val id: String,
        val email: String,
        @JsonProperty("verified_email")
        val verifiedEmail: Boolean,
        val name: String,
        @JsonProperty("given_name")
        val givenName: String,
        @JsonProperty("family_name")
        val familyName: String,
        val picture: String
    )

    fun callUserInfoApi(accessToken: String): String {
        val headers = HttpHeaders().apply {
            setBearerAuth(accessToken)
        }

        val request = HttpEntity<Unit>(headers)

        val response = restTemplate.exchange(
            URI(GOOGLE_USER_API_ENDPOINT),
            HttpMethod.GET,
            request,
            GoogleUserInfo::class.java
        )

        if (response.statusCode.isError) {
            throw BusinessException("구글 사용자 정보 요청에 실패했습니다.")
        }

        return response.body?.email ?: throw ServerException("구글로 부터 사용자 정보를 가져오는데 실패했습니다.")
    }
}