package com.project.scrumbleserver.service.member

import com.project.scrumbleserver.api.member.ApiGetMemberInfoResponse
import com.project.scrumbleserver.api.member.ApiPostMemberInfoRequest
import com.project.scrumbleserver.global.exception.BusinessException
import com.project.scrumbleserver.global.transaction.Transaction
import com.project.scrumbleserver.repository.member.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val transaction: Transaction,
    private val memberRepository: MemberRepository,
) {
    fun updateInfo(
        request: ApiPostMemberInfoRequest,
        memberRowid: Long,
    ) = transaction {
        val member =
            memberRepository.findByIdOrNull(memberRowid)
                ?: throw BusinessException("존재하지 않는 회원입니다.")

        member.also {
            it.nickname = request.nickname
            it.profileImageUrl = request.profileImageUrl
        }
    }

    fun findById(memberRowid: Long): ApiGetMemberInfoResponse =
        transaction.readOnly {
            val member =
                memberRepository.findByIdOrNull(memberRowid)
                    ?: throw BusinessException("존재하지 않는 회원입니다.")

            ApiGetMemberInfoResponse(
                rowid = member.rowid,
                email = member.email,
                nickname = member.nickname,
                profileImageUrl = member.profileImageUrl,
            )
        }
}
