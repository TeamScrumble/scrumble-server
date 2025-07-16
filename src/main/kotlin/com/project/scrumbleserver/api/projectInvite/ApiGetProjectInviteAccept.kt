package com.project.scrumbleserver.api.projectInvite

const val API_POST_PROJECT_INVITE_ACCEPT_PATH = "/api/v1/project-invite/{code}/accept"

data class ApiPostProjectInviteAcceptResponse(
    val projectInviteRowid: Long,
)
