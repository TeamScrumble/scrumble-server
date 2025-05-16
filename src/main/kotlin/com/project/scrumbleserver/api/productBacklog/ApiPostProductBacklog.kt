package com.project.scrumbleserver.api.productBacklog

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

object ApiPostProductBacklog {
    const val PATH = "/api/v1/product-backlog"
}

data class ApiPostProductBacklogRequest(
    @field:NotNull(message = "projectRowid는 필수입니다.")
    val projectRowid: Long,

    @field:NotBlank(message = "title은 필수입니다.")
    @field:Size(max = 30, message = "title은 100자 이하로 입력해야 합니다.")
    val title: String,

    @field:Size(max = 350, message = "description은 350자 이하로 입력해야 합니다.")
    val description: String,

    @field:NotNull(message = "priority는 필수입니다.")
    val priority: ProductBacklogPriority
)

data class ApiPostProductBacklogResponse(
    val productBacklogRowid: Long
)