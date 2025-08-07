package com.project.scrumbleserver.repository.projectMember

import com.project.scrumbleserver.domain.member.Member
import com.project.scrumbleserver.domain.project.Project
import com.project.scrumbleserver.domain.projectMember.ProjectMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProjectMemberRepository : JpaRepository<ProjectMember, Long> {
    fun findByProjectAndMember(
        project: Project,
        member: Member,
    ): ProjectMember?

    @Query("SELECT pm FROM project_member pm JOIN FETCH pm.member WHERE pm.project = :project")
    fun findByProject(project: Project): List<ProjectMember>
}
