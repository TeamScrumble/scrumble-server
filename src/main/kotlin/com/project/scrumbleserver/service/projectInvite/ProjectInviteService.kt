package com.project.scrumbleserver.service.projectInvite

import com.project.scrumbleserver.domain.member.Member
import com.project.scrumbleserver.domain.projectInvite.ProjectInvite
import com.project.scrumbleserver.global.excception.BusinessException
import com.project.scrumbleserver.global.transaction.Transaction
import com.project.scrumbleserver.repository.project.ProjectRepository
import com.project.scrumbleserver.repository.projectInvite.ProjectInviteRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProjectInviteService(
    private val transaction: Transaction,
    private val projectRepository: ProjectRepository,
    private val projectInviteRepository: ProjectInviteRepository
) {
    fun invite(
        projectRowid: Long,
        email: String
    ): Long {
        val projectInviteRowid = transaction {
            val project = projectRepository.findByIdOrNull(projectRowid)
                ?: throw BusinessException("프로젝트를 찾을 수 없습니다.")

            val projectInvite = projectInviteRepository.save(
                ProjectInvite(
                    project = project,
                    member = Member(
                        email = email,
                        nickname = "",
                        job = ""
                    )
                )
            )

            projectInvite.rowid
        }

        return projectInviteRowid
    }
}