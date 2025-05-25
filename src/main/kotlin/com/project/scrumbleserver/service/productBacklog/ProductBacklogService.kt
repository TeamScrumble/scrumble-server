package com.project.scrumbleserver.service.productBacklog

import com.project.scrumbleserver.api.productBacklog.ApiGetAllProductBacklogResponse
import com.project.scrumbleserver.api.productBacklog.ApiPostProductBacklogRequest
import com.project.scrumbleserver.domain.productBacklog.ProductBacklog
import com.project.scrumbleserver.global.excception.BusinessException
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

        return productBacklogList.map {
            ApiGetAllProductBacklogResponse(
                listOf(
                    ApiGetAllProductBacklogResponse.ProductBacklog(
                        productBacklogRowid = it.rowid,
                        title = it.title,
                        description = it.description,
                        priority = it.priority,
                        regDate = it.regDate
                    )
                )
            )
        }
    }
}