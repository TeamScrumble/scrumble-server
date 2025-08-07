package com.project.scrumbleserver.service.tag

import com.project.scrumbleserver.domain.productBacklog.ProductBacklog
import com.project.scrumbleserver.domain.project.Project
import com.project.scrumbleserver.domain.tag.ProductBacklogTag
import com.project.scrumbleserver.global.exception.BusinessException
import com.project.scrumbleserver.repository.tag.ProductBacklogTagRepository
import com.project.scrumbleserver.repository.tag.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductBacklogTagService(
    private val tagRepository: TagRepository,
    private val productBacklogTagRepository: ProductBacklogTagRepository,
) {
    @Transactional
    fun saveProductBacklogTags(
        project: Project,
        productBacklog: ProductBacklog,
        requestedTagIds: Set<Long>,
    ) {
        val tags = tagRepository.findAllByIdWithProject(requestedTagIds, project.rowid)

        if (tags.size != requestedTagIds.size) {
            throw BusinessException("존재하지 않는 태그가 포함되어 있습니다.")
        }

        val productBacklogTags =
            tags.map {
                ProductBacklogTag(
                    productBacklog = productBacklog,
                    tag = it,
                )
            }

        productBacklogTagRepository.saveAll(productBacklogTags)
    }

    @Transactional(readOnly = true)
    fun findAllByProductBacklogRowidList(productBacklogRowidList: Set<Long>): List<ProductBacklogTag> =
        productBacklogTagRepository.findAllByProductBacklogRowidInFetchJoin(productBacklogRowidList)
}
