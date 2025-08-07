package com.project.scrumbleserver.domain.projectMember

import com.project.scrumbleserver.global.exception.BusinessException

enum class ProjectMemberPermission(val level: Int) {
    OWNER(100),
    CAN_EDIT(20),
    CAN_VIEW(10);

    fun hasAtLeast(required: ProjectMemberPermission): Boolean {
        return this.level >= required.level
    }
}