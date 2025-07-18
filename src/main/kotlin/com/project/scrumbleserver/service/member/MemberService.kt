package com.project.scrumbleserver.service.member

import com.project.scrumbleserver.api.member.ApiPostMemberInfoRequest
import com.project.scrumbleserver.global.exception.BusinessException
import com.project.scrumbleserver.global.transaction.Transaction
import com.project.scrumbleserver.repository.member.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val transaction: Transaction,
    private val memberRepository: MemberRepository
) {

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