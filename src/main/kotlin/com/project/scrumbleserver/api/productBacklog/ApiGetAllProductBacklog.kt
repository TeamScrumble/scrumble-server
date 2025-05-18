package com.project.scrumbleserver.api.productBacklog

import com.project.scrumbleserver.domain.productBacklog.ProductBacklogPriority
import java.time.LocalDateTime

object ApiGetAllProductBacklog {
    const val PATH = "/api/v1/product-backlogs"

    data class Request(
        val projectRowid: Long
    )

    data class Response(
        val productBacklogRowid: Long,
        val title: String,
        val description: String,
        val priority: ProductBacklogPriority,
        val regDate: LocalDateTime
    )
}