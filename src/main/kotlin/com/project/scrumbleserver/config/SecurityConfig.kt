package com.project.scrumbleserver.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.project.scrumbleserver.repository.member.MemberRepository
import com.project.scrumbleserver.infra.jwt.JwtTokenProvider
import com.project.scrumbleserver.security.ForbiddenHandler
import com.project.scrumbleserver.security.SecurityFilter
import com.project.scrumbleserver.security.UnauthorizedHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtTokenProvider: JwtTokenProvider,
        objectMapper: ObjectMapper,
        memberRepository: MemberRepository,
        forbiddenHandler: ForbiddenHandler,
        unauthorizedHandler: UnauthorizedHandler,
    ): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers("/**").permitAll()
            }
            .addFilterBefore(
                SecurityFilter(jwtTokenProvider, objectMapper, memberRepository),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .exceptionHandling {
                it.authenticationEntryPoint(unauthorizedHandler)
                it.accessDeniedHandler(forbiddenHandler)
            }
            .build()
    }
}