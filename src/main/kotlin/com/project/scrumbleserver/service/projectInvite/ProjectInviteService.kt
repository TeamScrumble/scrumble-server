package com.project.scrumbleserver.service.projectInvite

import com.project.scrumbleserver.domain.projectInvite.ProjectInvite
import com.project.scrumbleserver.domain.projectMember.ProjectMember
import com.project.scrumbleserver.domain.projectMember.ProjectMemberPermission
import com.project.scrumbleserver.global.error.CommonError
import com.project.scrumbleserver.global.error.ProjectError
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
    private val memberRepository: MemberRepository,
) {
    fun invite(
        projectRowid: Long,
        email: String,
    ): Long {
        val projectInviteRowid =
            transaction {
                val project =
                    projectRepository.findByIdOrNull(projectRowid)
                        ?: throw BusinessException(ProjectError.NOT_FOUND_PROJECT)

                val member = memberRepository.findByEmail(email) ?: throw BusinessException(CommonError.NOT_FOUND_MEMBER)

                val projectInvite =
                    projectInviteRepository.save(
                        ProjectInvite(
                            project = project,
                            member = member,
                        ),
                    )

                projectInvite.rowid
            }

        return projectInviteRowid
    }

    fun inviteAccept(code: String): Long =
        transaction {
            val projectInvite =
                projectInviteRepository.findByUuid(code)
                    ?: throw BusinessException(ProjectError.NOT_FOUND_INVITATION_CODE)

            val projectMember =
                projectMemberRepository.save(
                    ProjectMember(
                        project = projectInvite.project,
                        member = projectInvite.member,
                        permission = ProjectMemberPermission.CAN_VIEW,
                    ),
                )

            projectMember.rowid
        }
}
