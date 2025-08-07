package com.project.scrumbleserver.repository.member

import com.project.scrumbleserver.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?

    fun findAllByRowidIn(rowids: List<Long>): List<Member>
}
