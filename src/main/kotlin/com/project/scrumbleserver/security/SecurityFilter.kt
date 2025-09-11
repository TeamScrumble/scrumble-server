package com.project.scrumbleserver.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.project.scrumbleserver.global.api.ApiResponse
import com.project.scrumbleserver.global.api.ErrorResponse
import com.project.scrumbleserver.global.error.AuthError
import com.project.scrumbleserver.infra.jwt.JwtTokenProvider
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class SecurityFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val objectMapper: ObjectMapper,
) : OncePerRequestFilter() {
    private class UserNotFoundException : RuntimeException()

    companion object {
        val EMPTY_ROLE = emptySet<GrantedAuthority>()
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val accessToken = extractToken(request)

        accessToken?.let { token ->
            runCatching {
                val userRowid = jwtTokenProvider.decodeToken(token)
                val authentication = UsernamePasswordAuthenticationToken(userRowid, null, EMPTY_ROLE)
                SecurityContextHolder.getContext().authentication = authentication
            }.getOrElse { e ->
                val code = when (e) {
                    is ExpiredJwtException -> AuthError.TOKEN_EXPIRED
                    is UserNotFoundException -> AuthError.NOT_FOUND_MEMBER
                    else -> null
                }

                code?.let {
                    sendErrorResponse(response, code)
                    return
                }
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val bearer = request.getHeader("Authorization")
        if (!bearer.isNullOrBlank() && bearer.startsWith("Bearer ")) {
            return bearer.substring(7)
        }

        return request.cookies?.firstOrNull { it.name == "access_token" }?.value
    }

    private fun sendErrorResponse(
        response: HttpServletResponse,
        code: AuthError,
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        val error = ApiResponse.of(ErrorResponse.from(code))
        objectMapper.writeValue(response.writer, error)
    }
}
