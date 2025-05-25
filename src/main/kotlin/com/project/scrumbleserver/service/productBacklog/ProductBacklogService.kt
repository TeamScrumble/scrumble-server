package com.project.scrumbleserver.service.productBacklog

import com.project.scrumbleserver.api.productBacklog.ApiGetAllProductBacklogResponse
import com.project.scrumbleserver.api.productBacklog.ApiPostProductBacklogRequest
import com.project.scrumbleserver.domain.productBacklog.ProductBacklog
import com.project.scrumbleserver.domain.productBacklogTag.ProductBacklogTag
import com.project.scrumbleserver.domain.tag.Tag
import com.project.scrumbleserver.global.excception.BusinessException
import com.project.scrumbleserver.repository.productBacklog.ProductBacklogRepository
import com.project.scrumbleserver.repository.productBacklogTag.ProductBacklogTagRepository
import com.project.scrumbleserver.repository.project.ProjectRepository
import com.project.scrumbleserver.repository.tag.TagRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductBacklogService(
    private val productBacklogRepository: ProductBacklogRepository,
    private val productBacklogTagRepository: ProductBacklogTagRepository,
    private val projectRepository: ProjectRepository,
    private val tagRepository: TagRepository,
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

        saveProductBacklogTags(project.rowid, productBacklog, request.tags)

        return productBacklog.rowid
    }

    private fun saveProductBacklogTags(
        projectRowid: Long,
        productBacklog: ProductBacklog,
        requestedTagIds: Set<Long>
    ) {
        val tags = tagRepository.findAllByRowidIsIn(requestedTagIds)

        validateTags(projectRowid, tags, requestedTagIds)

        val tagMappings = tags.map {
            ProductBacklogTag(
                productBacklog = productBacklog,
                tag = it
            )
        }

        productBacklogTagRepository.saveAll(tagMappings)
    }

    private fun validateTags(
        projectRowid: Long,
        tags: List<Tag>,
        requestedTagIds: Set<Long>
    ) {
        if (tags.size != requestedTagIds.size) {
            throw BusinessException("존재하지 않는 태그가 포함되어 있습니다.")
        }

        if (tags.any { it.project.rowid != projectRowid }) {
            throw BusinessException("해당 프로젝트에 속하지 않은 태그가 포함되어 있습니다.")
        }
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
                        tags = it.tags.map { tag ->
                            ApiGetAllProductBacklogResponse.ProductBacklogTag(
                                tag.rowid,
                                tag.tag.title,
                                tag.tag.color
                            )
                        },
                        regDate = it.regDate
                    )
                )
            )
        }
    }
}