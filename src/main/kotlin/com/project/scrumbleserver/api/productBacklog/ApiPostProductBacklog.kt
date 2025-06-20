package com.project.scrumbleserver.api.productBacklog

import com.project.scrumbleserver.domain.productBacklog.ProductBacklogPriority
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

const val API_POST_PRODUCT_BACKLOG_PATH = "/api/v1/product-backlog"

data class ApiPostProductBacklogRequest(
    @field:NotNull(message = "projectRowid는 필수입니다.")
    val projectRowid: Long,

    @field:NotBlank(message = "title은 필수입니다.")
    @field:Size(max = 30, message = "title은 100자 이하로 입력해야 합니다.")
    val title: String,

    @field:Size(max = 350, message = "description은 350자 이하로 입력해야 합니다.")
    val description: String,

    @field:NotNull(message = "priority는 필수입니다.")
    val priority: ProductBacklogPriority,

    @field:Size(max = 10, message = "최대 10개의 태그만 지정할 수 있습니다.")
    val tags: Set<Long>,
)

data class ApiPostProductBacklogResponse(
    val productBacklogRowid: Long
)