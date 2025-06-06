package com.project.scrumbleserver.infra.storage

import com.project.scrumbleserver.global.exception.BusinessException
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.util.Base64

@Component
class ImageUploader(
    private val restTemplate: RestTemplate,
) {
    companion object {
        private const val CLIENT_ID = "fe06e971ca64f17"
        private const val API_URL = "https://api.imgur.com/3/image"
    }

    private data class Response(
        val success: Boolean,
        val status: Int,
        val data: Data,
    ) {
        data class Data(
            val link: String,
        )
    }

    fun upload(byteArray: ByteArray): String {
        val headers = HttpHeaders().apply {
            contentType = MediaType.MULTIPART_FORM_DATA
            set("Authorization", "Client-ID $CLIENT_ID")
        }

        val body: MultiValueMap<String, Any> = LinkedMultiValueMap()
        body.add("image", Base64.getEncoder().encodeToString(byteArray))

        val request = HttpEntity(body, headers)
        val response = restTemplate
            .exchange(API_URL, HttpMethod.POST, request, Response::class.java)

        return response.body?.data?.link ?: throw BusinessException("이미지 업로드에 실패 했습니다.")
    }
}