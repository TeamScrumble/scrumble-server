package com.project.scrumbleserver.service.productBacklog

import com.project.scrumbleserver.api.productBacklog.ApiGetAllProductBacklogResponse
import com.project.scrumbleserver.api.productBacklog.ApiPostProductBacklogAssignRequest
import com.project.scrumbleserver.api.productBacklog.ApiPostProductBacklogRequest
import com.project.scrumbleserver.domain.assignee.Assignee
import com.project.scrumbleserver.domain.productBacklog.ProductBacklog
import com.project.scrumbleserver.domain.tag.Tag
import com.project.scrumbleserver.global.exception.BusinessException
import com.project.scrumbleserver.global.transaction.Transaction
import com.project.scrumbleserver.repository.assignee.AssigneeRepository
import com.project.scrumbleserver.repository.member.MemberRepository
import com.project.scrumbleserver.repository.productBacklog.ProductBacklogRepository
import com.project.scrumbleserver.repository.project.ProjectRepository
import com.project.scrumbleserver.service.tag.ProductBacklogTagService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProductBacklogService(
    private val transaction: Transaction,
    private val productBacklogRepository: ProductBacklogRepository,
    private val projectRepository: ProjectRepository,
    private val productBacklogTagService: ProductBacklogTagService,
    private val memberRepository: MemberRepository,
    private val assigneeRepository: AssigneeRepository
) {

    fun insert(request: ApiPostProductBacklogRequest): Long = transaction {
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

        assignMembers(request.assignees, productBacklog)

        productBacklog.rowid
    }

    fun assign(request: ApiPostProductBacklogAssignRequest) = transaction {
        val productBacklog = productBacklogRepository.findByIdOrNull(request.productBacklogRowid)
            ?: throw BusinessException("프로덕트 백로그를 찾을 수 없습니다.")

        val registeredAssignees = assigneeRepository.findAllByProductBacklog(productBacklog)
        registeredAssignees.forEach { assignees ->
            assignees.delete()
        }

        assignMembers(request.assignees, productBacklog)
    }

    private fun assignMembers(
        memberRowidSet: Set<Long>,
        productBacklog: ProductBacklog
    ) {
        val members = memberRepository.findAllByRowid(memberRowidSet.toList())

        if (members.size != memberRowidSet.size) {
            throw BusinessException("존재하지 않은 멤버가 포함되어 있습니다.")
        }

        members.forEach { member ->
            val assignee = Assignee(
                member = member,
                productBacklog = productBacklog
            )
            assigneeRepository.save(assignee)
        }
    }

    fun findAll(projectRowid: Long): List<ApiGetAllProductBacklogResponse> = transaction.readOnly {
        val productBacklogList = productBacklogRepository.findAllByProjectRowid(projectRowid)

        val productBacklogsRowidList = productBacklogList.map { it.rowid }.toSet()
        val productBacklogTags = productBacklogTagService.findAllByProductBacklogRowidList(productBacklogsRowidList)

        val tagMap: Map<Long, List<Tag>> = productBacklogTags
            .groupBy { it.productBacklog.rowid }
            .mapValues { entry -> entry.value.map { it.tag } }

        productBacklogList.map { productBacklog ->
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

