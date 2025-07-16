package com.project.scrumbleserver.service.projectInvite

import com.project.scrumbleserver.domain.projectInvite.ProjectInvite
import com.project.scrumbleserver.domain.projectMember.ProjectMember
import com.project.scrumbleserver.domain.projectMember.ProjectMemberPermission
import com.project.scrumbleserver.global.exception.BusinessException
import com.project.scrumbleserver.global.transaction.Transaction
import com.project.scrumbleserver.repository.member.MemberRepository
import com.project.scrumbleserver.repository.project.ProjectRepository
import com.project.scrumbleserver.repository.projectInvite.ProjectInviteRepository
import com.project.scrumbleserver.repository.projectMember.ProjectMemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProjectInviteService(
    private val transaction: Transaction,
    private val projectRepository: ProjectRepository,
    private val projectInviteRepository: ProjectInviteRepository,
    private val projectMemberRepository: ProjectMemberRepository,
    private val memberRepository: MemberRepository
) {
    fun invite(
        projectRowid: Long,
        email: String
    ): Long {
        val projectInviteRowid = transaction {
            val project = projectRepository.findByIdOrNull(projectRowid)
                ?: throw BusinessException("프로젝트를 찾을 수 없습니다.")

            val member = memberRepository.findByEmail(email) ?: throw BusinessException("회원을 찾을 수 없습니다.")

            val projectInvite = projectInviteRepository.save(
                ProjectInvite(
                    project = project,
                    member = member
                )
            )

            projectInvite.rowid
        }

        return projectInviteRowid
    }

    fun inviteAccept(
        code: String
    ): Long = transaction {
        val projectInvite = projectInviteRepository.findByUuid(code)
            ?: throw BusinessException("초대 코드를 찾을 수 없습니다.")

        val projectMember = projectMemberRepository.save(
            ProjectMember(
                project = projectInvite.project,
                member = projectInvite.member,
                permission = ProjectMemberPermission.CAN_VIEW
            )
        )

        projectMember.rowid
    }
}