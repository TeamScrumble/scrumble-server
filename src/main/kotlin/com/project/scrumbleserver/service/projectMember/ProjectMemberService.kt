package com.project.scrumbleserver.service.projectMember

import com.project.scrumbleserver.domain.projectMember.ProjectMember
import com.project.scrumbleserver.domain.projectMember.ProjectMemberPermission
import com.project.scrumbleserver.global.exception.BusinessException
import com.project.scrumbleserver.global.transaction.Transaction
import com.project.scrumbleserver.repository.member.MemberRepository
import com.project.scrumbleserver.repository.project.ProjectRepository
import com.project.scrumbleserver.repository.projectMember.ProjectMemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProjectMemberService(
    private val transaction: Transaction,
    private val projectRepository: ProjectRepository,
    private val projectMemberRepository: ProjectMemberRepository,
    private val memberRepository: MemberRepository
) {
    companion object {
        private const val MEMBER_NOT_FOUND_MSG = "존재하지 않는 회원입니다."
        private const val PROJECT_NOT_FOUND_MSG = "존재하지 않는 프로젝트입니다."
        private const val NOT_PROJECT_MEMBER_MSG = "프로젝트에 속한 회원이 아닙니다."
        private const val MUST_HAVE_AT_LEAST_ONE_OWNER_MSG = "프로젝트에 최소 한 명 이상의 OWNER 권한이 존재해야 합니다."
        private const val INSUFFICIENT_PROJECT_PERMISSION_MSG = "해당 작업을 수행할 권한이 없습니다."
    }

    fun assertPermission(projectRowid: Long, memberRowid: Long, required: ProjectMemberPermission) {
        val member = memberRepository.findByIdOrNull(memberRowid)
            ?: throw BusinessException(MEMBER_NOT_FOUND_MSG)

        val project = projectRepository.findByIdOrNull(projectRowid)
            ?: throw BusinessException(PROJECT_NOT_FOUND_MSG)

        val projectMember = projectMemberRepository.findByProjectAndMember(project, member)
            ?: throw BusinessException(NOT_PROJECT_MEMBER_MSG)

        if(!projectMember.permission.hasAtLeast(required)) {
            throw BusinessException(INSUFFICIENT_PROJECT_PERMISSION_MSG)
        }
    }

    fun edit(
        projectRowid: Long,
        targetMemberRowid: Long,
        targetPermission: ProjectMemberPermission,
        loginMemberRowid: Long,
    ): Long = transaction {
        memberRepository.findByIdOrNull(loginMemberRowid)
            ?: throw BusinessException(MEMBER_NOT_FOUND_MSG)

        val project = projectRepository.findByIdOrNull(projectRowid)
            ?: throw BusinessException(PROJECT_NOT_FOUND_MSG)

        val projectMembers = projectMemberRepository.findByProject(project)
        val targetMember = projectMembers.find { it.member.rowid == targetMemberRowid }
            ?: throw BusinessException(NOT_PROJECT_MEMBER_MSG)

        if (targetMember.permission == targetPermission) {
            return@transaction targetMember.rowid
        }

        checkIfLastOwner(projectMembers, targetMember, targetPermission)

        targetMember.permission = targetPermission
        projectMemberRepository.save(targetMember)

        return@transaction targetMember.rowid
    }

    private fun checkIfLastOwner(
        projectMembers: List<ProjectMember>,
        targetMember: ProjectMember,
        targetPermission: ProjectMemberPermission
    ) {
        val isTargetOwner = targetMember.permission == ProjectMemberPermission.OWNER
        val isChangingFromOwner = isTargetOwner && targetPermission != ProjectMemberPermission.OWNER
        val ownerCount = projectMembers.count { it.permission == ProjectMemberPermission.OWNER }

        if (isChangingFromOwner && ownerCount == 1) {
            throw BusinessException(MUST_HAVE_AT_LEAST_ONE_OWNER_MSG)
        }
    }
}