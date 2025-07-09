package com.project.scrumbleserver.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.global.api.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.security.access.AccessDeniedException

@Component
class ForbiddenHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        response.contentType = "application/json; charset=UTF-8"
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_FORBIDDEN

        val responseWriter = response.writer
        val errorResponse = ApiResponse.of(ErrorResponse("Forbidden"))
        objectMapper.writeValue(responseWriter, errorResponse)
        responseWriter.flush()
    }
}
