package com.project.scrumbleserver.service.tag

import com.project.scrumbleserver.domain.productBacklog.ProductBacklog
import com.project.scrumbleserver.domain.project.Project
import com.project.scrumbleserver.domain.tag.ProductBacklogTag
import com.project.scrumbleserver.domain.tag.Tag
import com.project.scrumbleserver.global.excception.BusinessException
import com.project.scrumbleserver.repository.tag.ProductBacklogTagRepository
import com.project.scrumbleserver.repository.tag.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductBacklogTagService(
    private val tagRepository: TagRepository,
    private val productBacklogTagRepository: ProductBacklogTagRepository
) {
    @Transactional
    fun saveProductBacklogTags(
        project: Project,
        productBacklog: ProductBacklog,
        requestedTagIds: Set<Long>
    ) {
        val tags = tagRepository.findAllById(requestedTagIds)

        validateTags(tags, requestedTagIds, project.rowid)

        val productBacklogTags = tags.map {
            ProductBacklogTag(
                productBacklog = productBacklog,
                tag = it
            )
        }

        productBacklogTagRepository.saveAll(productBacklogTags)
    }

    private fun validateTags(tags: List<Tag>, requestedTagIds: Set<Long>, projectRowid: Long) {
        if (tags.size != requestedTagIds.size) {
            throw BusinessException("존재하지 않는 태그가 포함되어 있습니다.")
        }

        if (tags.any { it.project.rowid != projectRowid }) {
            throw BusinessException("해당 프로젝트에 속하지 않은 태그가 포함되어 있습니다.")
        }
    }

    @Transactional(readOnly = true)
    fun findAllByProductBacklogRowidList(productBacklogRowidList: Set<Long>): List<ProductBacklogTag> {
        return productBacklogTagRepository.findAllByProductBacklogRowidInFetchJoin(productBacklogRowidList)
    }
}