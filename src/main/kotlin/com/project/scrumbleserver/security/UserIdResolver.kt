package com.project.scrumbleserver.security

import com.project.scrumbleserver.global.exception.UnauthorizedException
import org.springframework.core.MethodParameter
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class UserIdResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(RequestUserRowid::class.java) &&
            parameter.parameterType == Long::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: org.springframework.web.bind.support.WebDataBinderFactory?,
    ): Any? {
        val auth = SecurityContextHolder.getContext().authentication

        if (auth is UsernamePasswordAuthenticationToken && auth.principal is Long) {
            return auth.principal as Long
        }

        throw UnauthorizedException("Unauthorized")
    }
}
