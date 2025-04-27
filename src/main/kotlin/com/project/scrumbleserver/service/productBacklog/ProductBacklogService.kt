package com.project.scrumbleserver.service.productBacklog

import com.project.scrumbleserver.api.productBacklog.ApiPostProductBacklog
import com.project.scrumbleserver.domain.productBacklog.ProductBacklog
import com.project.scrumbleserver.global.excception.BusinessException
import com.project.scrumbleserver.repository.productBacklog.ProductBacklogRepository
import com.project.scrumbleserver.repository.project.ProjectRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProductBacklogService(
    private val productBacklogRepository: ProductBacklogRepository,
    private val projectRepository: ProjectRepository
) {
    fun insert(request: ApiPostProductBacklog.Request): Long {
        val project = projectRepository.findByIdOrNull(request.projectRowid)
            ?: throw BusinessException("프로젝트를 찾을 수 없습니다.")

        val productBacklog = productBacklogRepository.save(
            ProductBacklog(
                project = project,
                title = request.title,
                description = request.description
            )
        )

        return productBacklog.rowid
    }

}