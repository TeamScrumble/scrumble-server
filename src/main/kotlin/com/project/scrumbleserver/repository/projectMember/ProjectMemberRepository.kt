package com.project.scrumbleserver.repository.projectMember

import com.project.scrumbleserver.domain.projectMember.ProjectMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectMemberRepository : JpaRepository<ProjectMember, Long>