package com.project.scrumbleserver.repository.assignee

import com.project.scrumbleserver.domain.assignee.Assignee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AssigneeRepository : JpaRepository<Assignee, Long>