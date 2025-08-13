package com.project.scrumbleserver.service.projectMember

import com.project.scrumbleserver.api.projectMember.ApiGetProjectMembersResponse
import com.project.scrumbleserver.domain.projectMember.ProjectMember
import com.project.scrumbleserver.domain.projectMember.ProjectMemberPermission
import com.project.scrumbleserver.global.error.CommonError
import com.project.scrumbleserver.global.error.ProjectError
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
    private val memberRepository: MemberRepository,
) {
    fun assertPermission(
        projectRowid: Long,
        memberRowid: Long,
        required: ProjectMemberPermission,
    ) = transaction.readOnly {
        val member =
            memberRepository.findByIdOrNull(memberRowid)
                ?: throw BusinessException(CommonError.MEMBER_NOT_FOUND)

        val project =
            projectRepository.findByIdOrNull(projectRowid)
                ?: throw BusinessException(ProjectError.PROJECT_NOT_FOUND)

        val projectMember =
            projectMemberRepository.findByProjectAndMember(project, member)
                ?: throw BusinessException(ProjectError.NOT_PROJECT_MEMBER)

        if (!projectMember.permission.hasAtLeast(required)) {
            throw BusinessException(ProjectError.INSUFFICIENT_PROJECT_PERMISSION)
        }
    }

    fun findAllByProjectRowid(projectRowid: Long): ApiGetProjectMembersResponse = transaction.readOnly {
        val project = projectRepository.findByIdOrNull(projectRowid)
            ?: throw BusinessException(ProjectError.PROJECT_NOT_FOUND)

        val projectMembers = projectMemberRepository.findByProject(project)

        ApiGetProjectMembersResponse(
            projectMembers.map { projectMember ->
                ApiGetProjectMembersResponse.Member(
                    memberRowid = projectMember.member.rowid,
                    email = projectMember.member.email,
                    profileImageUrl = projectMember.member.profileImageUrl,
                    permission = projectMember.permission.name,
                )
            },
        )
    }

    fun editPermission(
        projectRowid: Long,
        targetMemberRowid: Long,
        targetPermission: ProjectMemberPermission,
        loginMemberRowid: Long,
    ): Long = transaction {
        memberRepository.findByIdOrNull(loginMemberRowid)
            ?: throw BusinessException(CommonError.MEMBER_NOT_FOUND)

        val project = projectRepository.findByIdOrNull(projectRowid)
                ?: throw BusinessException(ProjectError.PROJECT_NOT_FOUND)

        val projectMembers = projectMemberRepository.findByProject(project)
        val targetMember = projectMembers.find { it.member.rowid == targetMemberRowid }
            ?: throw BusinessException(ProjectError.NOT_PROJECT_MEMBER)

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
        targetPermission: ProjectMemberPermission,
    ) {
        val isTargetOwner = targetMember.permission == ProjectMemberPermission.OWNER
        val isChangingFromOwner = isTargetOwner && targetPermission != ProjectMemberPermission.OWNER
        val ownerCount = projectMembers.count { it.permission == ProjectMemberPermission.OWNER }

        if (isChangingFromOwner && ownerCount == 1) {
            throw BusinessException(ProjectError.MUST_HAVE_AT_LEAST_ONE_OWNER)
        }
    }

    fun editRole(
        projectRowid: Long,
        memberRowid: Long,
        role: String,
    ) = transaction {
        val project = projectRepository.findByIdOrNull(projectRowid)
            ?: throw BusinessException(ProjectError.PROJECT_NOT_FOUND)
        val member = memberRepository.findByIdOrNull(memberRowid)
            ?: throw BusinessException(CommonError.MEMBER_NOT_FOUND)
        val projectMember = projectMemberRepository.findByProjectAndMember(project, member)
            ?: throw BusinessException(ProjectError.NOT_PROJECT_MEMBER)

        projectMember.role = role

        return@transaction projectMember.rowid
    }
}
