package com.project.scrumbleserver.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.global.api.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class UnauthorizedHandler(
    private val objectMapper: ObjectMapper,
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException?,
    ) {
        response.contentType = "application/json; charset=UTF-8"
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_UNAUTHORIZED

        val responseWriter = response.writer
        val errorResponse = ApiResponse.of(ErrorResponse("Unauthorized"))
        objectMapper.writeValue(responseWriter, errorResponse)
        responseWriter.flush()
    }
}
