package com.project.scrumbleserver.domain.projectMember

enum class ProjectMemberPermission(
    val level: Int,
) {
    OWNER(100),
    CAN_EDIT(20),
    CAN_VIEW(10),
    ;

    fun hasAtLeast(required: ProjectMemberPermission): Boolean = this.level >= required.level
}
