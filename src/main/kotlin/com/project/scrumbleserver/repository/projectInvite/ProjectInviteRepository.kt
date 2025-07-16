package com.project.scrumbleserver.repository.projectInvite

import com.project.scrumbleserver.domain.projectInvite.ProjectInvite
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectInviteRepository : JpaRepository<ProjectInvite, Long> {
    fun findByUuid(uuid: String): ProjectInvite?
}