package com.project.scrumbleserver.repository.project

import com.project.scrumbleserver.domain.project.Project
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepository : JpaRepository<Project, Long>
