package com.project.scrumbleserver.infra.jwt

import com.project.scrumbleserver.global.exception.UnauthorizedException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    private val jwtProperties: JwtProperties,
) {
    private val key: SecretKey = Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())

    fun provideAccessToken(userId: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtProperties.tokenExpires)

        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun provideRefreshToken(): String {
        return UUID.randomUUID().toString()
    }

    fun decodeToken(token: String): Long {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
            .toLongOrNull()
            ?: throw UnauthorizedException("유효하지 사용자 일련번호가 아닙니다.")
    }
}