package com.project.scrumbleserver.api.productBacklog

import jakarta.validation.constraints.NotNull

const val API_POST_PRODUCT_BACKLOG_ASSIGN_PATH = "/api/v1/product-backlog/assign"

data class ApiPostProductBacklogAssignRequest(
    @field:NotNull(message = "productBacklogRowid는 비어있을 수 없습니다.")
    val productBacklogRowid: Long,
    @field:NotNull(message = "담당자는 비어있을 수 없습니다.")
    val assignees: Set<Long>,
)

data class ApiPostProductBacklogAssignResponse(
    val productBacklogRowid: Long,
)
