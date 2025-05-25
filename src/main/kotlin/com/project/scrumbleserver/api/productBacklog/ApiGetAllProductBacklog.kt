package com.project.scrumbleserver.api.productBacklog

import com.project.scrumbleserver.domain.productBacklog.ProductBacklogPriority
import java.time.LocalDateTime

const val API_GET_ALL_PRODUCT_BACKLOG_PATH = "/api/v1/product-backlogs"

data class ApiGetAllProductBacklogRequest(
    val projectRowid: Long
)

data class ApiGetAllProductBacklogResponse(
    val productBacklogs: List<ProductBacklog>
) {
    data class ProductBacklog(
        val productBacklogRowid: Long,
        val title: String,
        val description: String,
        val priority: ProductBacklogPriority,
        val regDate: LocalDateTime
    )
}
