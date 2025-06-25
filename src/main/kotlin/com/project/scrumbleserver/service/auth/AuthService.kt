package com.project.scrumbleserver.service.auth

import com.project.scrumbleserver.api.auth.ApiPostMemberInfoRequest
import com.project.scrumbleserver.domain.member.Member
import com.project.scrumbleserver.repository.member.MemberRepository
import com.project.scrumbleserver.global.exception.BusinessException
import com.project.scrumbleserver.global.transaction.Transaction
import com.project.scrumbleserver.infra.cache.CacheStorage
import com.project.scrumbleserver.infra.jwt.JwtTokenProvider
import com.project.scrumbleserver.infra.oauth.google.GoogleAuthenticator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val googleAuthenticator: GoogleAuthenticator,
    private val jwtTokenProvider: JwtTokenProvider,
    private val cacheStorage: CacheStorage,
    private val transaction: Transaction,
) {
    companion object {
        private const val REFRESH_TOKEN_PREFIX = "REFRESH_TOKEN_"
        private const val SEVEN_DAYS = 7 * 24 * 60 * 60 * 1000L
    }

    data class AuthToken(
        val accessToken: String,
        val refreshToken: String,
    )

    fun login(code: String): AuthToken {
        val userEmail = googleAuthenticator.authenticate(code)
        val member = transaction {
            memberRepository.findByEmail(userEmail) ?: run {
                memberRepository.save(Member(email = userEmail))
            }
        }

        val accessToken = jwtTokenProvider.provideAccessToken(member.rowid)
        val refreshToken = jwtTokenProvider.provideRefreshToken()

        cacheStorage.put("${REFRESH_TOKEN_PREFIX}${refreshToken}", member.rowid.toString(), SEVEN_DAYS)

        return AuthToken(accessToken, refreshToken)
    }

    fun refresh(refreshToken: String): AuthToken {
        val memberId = cacheStorage.get("${REFRESH_TOKEN_PREFIX}${refreshToken}")?.toLongOrNull()
            ?: throw BusinessException("유효하지 않은 인증 정보 입니다.")

        val accessToken = jwtTokenProvider.provideAccessToken(memberId)
        val refreshToken = jwtTokenProvider.provideRefreshToken()

        cacheStorage.put("${REFRESH_TOKEN_PREFIX}${refreshToken}", memberId.toString(), SEVEN_DAYS)

        return AuthToken(accessToken, refreshToken)
    }

    fun enterAdditionalInfo(request: ApiPostMemberInfoRequest, memberRowid: Long) = transaction {
        val member = memberRepository.findByIdOrNull(memberRowid)
            ?: throw BusinessException("존재하지 않는 회원입니다.")

        member.also {
            it.nickname = request.nickname
            it.job = request.job
            it.profileImageUrl = request.profileImageUrl
        }
    }
}
