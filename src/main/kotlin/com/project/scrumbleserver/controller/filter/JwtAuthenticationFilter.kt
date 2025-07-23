package com.project.scrumbleserver.controller.filter

import com.project.scrumbleserver.infra.jwt.JwtTokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {
    companion object {
        const val USER_ROWID = "userRowid"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader("Authorization")?.removePrefix("Bearer ")?.trim()
        logger.info("token = {$token}")

        if (!token.isNullOrBlank()) {
            val userRowid = jwtTokenProvider.decodeToken(token)
            request.setAttribute(USER_ROWID, userRowid)
        }

        filterChain.doFilter(request, response)
    }
}