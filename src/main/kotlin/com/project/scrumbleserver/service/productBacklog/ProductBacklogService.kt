package com.project.scrumbleserver.service.productBacklog

import com.project.scrumbleserver.api.productBacklog.ApiGetAllProductBacklogResponse
import com.project.scrumbleserver.api.productBacklog.ApiPostProductBacklogRequest
import com.project.scrumbleserver.domain.productBacklog.ProductBacklog
import com.project.scrumbleserver.domain.tag.Tag
import com.project.scrumbleserver.global.exception.BusinessException
import com.project.scrumbleserver.repository.productBacklog.ProductBacklogRepository
import com.project.scrumbleserver.repository.project.ProjectRepository
import com.project.scrumbleserver.service.tag.ProductBacklogTagService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductBacklogService(
    private val productBacklogRepository: ProductBacklogRepository,
    private val projectRepository: ProjectRepository,
    private val productBacklogTagService: ProductBacklogTagService,
) {

    @Transactional
    fun insert(request: ApiPostProductBacklogRequest): Long {
        val project = projectRepository.findByIdOrNull(request.projectRowid)
            ?: throw BusinessException("프로젝트를 찾을 수 없습니다.")

        val productBacklog = productBacklogRepository.save(
            ProductBacklog(
                project = project,
                title = request.title,
                description = request.description,
                priority = request.priority,
            )
        )

        productBacklogTagService.saveProductBacklogTags(
            project,
            productBacklog,
            request.tags
        )

        return productBacklog.rowid
    }

    @Transactional(readOnly = true)
    fun findAll(projectRowid: Long): List<ApiGetAllProductBacklogResponse> {
        val productBacklogList = productBacklogRepository.findAllByProjectRowid(projectRowid)

        val productBacklogsRowidList = productBacklogList.map { it.rowid }.toSet()
        val productBacklogTags = productBacklogTagService.findAllByProductBacklogRowidList(productBacklogsRowidList)

        val tagMap: Map<Long, List<Tag>> = productBacklogTags
            .groupBy { it.productBacklog.rowid }
            .mapValues { entry -> entry.value.map { it.tag } }

        return productBacklogList.map { productBacklog ->
            ApiGetAllProductBacklogResponse(
                listOf(
                    ApiGetAllProductBacklogResponse.ProductBacklog(
                        productBacklogRowid = productBacklog.rowid,
                        title = productBacklog.title,
                        description = productBacklog.description,
                        priority = productBacklog.priority,
                        tags = tagMap[productBacklog.rowid].orEmpty()
                            .map { tag ->
                                ApiGetAllProductBacklogResponse.ProductBacklogTag(
                                    productBacklogTagRowid = tag.rowid,
                                    title = tag.title,
                                    color = tag.color,
                                )
                            },
                        regDate = productBacklog.regDate
                    )
                )
            )
        }
    }
}

