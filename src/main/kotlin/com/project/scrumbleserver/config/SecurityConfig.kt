package com.project.scrumbleserver.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.project.scrumbleserver.infra.jwt.JwtTokenProvider
import com.project.scrumbleserver.repository.member.MemberRepository
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
        forbiddenHandler: ForbiddenHandler,
        unauthorizedHandler: UnauthorizedHandler,
    ): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .cors {}
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/api/v1/auth/**",
                        "/swagger-ui/**",
                        "/error-codes.html"
                    )
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }.addFilterBefore(
                SecurityFilter(jwtTokenProvider, objectMapper),
                UsernamePasswordAuthenticationFilter::class.java,
            ).exceptionHandling {
                it.authenticationEntryPoint(unauthorizedHandler)
                it.accessDeniedHandler(forbiddenHandler)
            }.build()
}
