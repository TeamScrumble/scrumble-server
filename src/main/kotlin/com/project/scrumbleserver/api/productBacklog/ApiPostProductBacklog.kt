package com.project.scrumbleserver.api.productBacklog

object ApiPostProductBacklog {
    const val PATH = "/api/v1/product-backlog"

    data class Request(
        val projectRowid: Long,
        val title: String,
        val description: String,
        val priority: ProductBacklogPriority,
    )

    data class Response(
        val productBacklogRowid: Long
    )
}