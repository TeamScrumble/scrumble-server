package com.project.scrumbleserver.service.productBacklog

import com.project.scrumbleserver.api.productBacklog.ApiGetAllProductBacklogResponse
import com.project.scrumbleserver.api.productBacklog.ApiPostProductBacklogAssignRequest
import com.project.scrumbleserver.api.productBacklog.ApiPostProductBacklogRequest
import com.project.scrumbleserver.domain.assignee.Assignee
import com.project.scrumbleserver.domain.productBacklog.ProductBacklog
import com.project.scrumbleserver.domain.tag.Tag
import com.project.scrumbleserver.global.error.AuthError
import com.project.scrumbleserver.global.error.BacklogError
import com.project.scrumbleserver.global.error.CommonError
import com.project.scrumbleserver.global.error.ProjectError
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
    private val assigneeRepository: AssigneeRepository,
) {
    fun insert(
        request: ApiPostProductBacklogRequest,
        userRowid: Long,
    ): Long =
        transaction {
            val project =
                projectRepository.findByIdOrNull(request.projectRowid)
                    ?: throw BusinessException(ProjectError.NOT_FOUND_PROJECT)

            val creator =
                memberRepository.findByIdOrNull(userRowid)
                    ?: throw BusinessException(AuthError.NOT_FOUND_MEMBER)

            val productBacklog =
                productBacklogRepository.save(
                    ProductBacklog(
                        project = project,
                        creator = creator,
                        title = request.title,
                        description = request.description,
                        priority = request.priority,
                    ),
                )

            productBacklogTagService.saveProductBacklogTags(
                project,
                productBacklog,
                request.tags,
            )

            assignMembers(request.assignees, productBacklog)

            productBacklog.rowid
        }

    fun assign(request: ApiPostProductBacklogAssignRequest) =
        transaction {
            val productBacklog =
                productBacklogRepository.findByIdOrNull(request.productBacklogRowid)
                    ?: throw BusinessException(BacklogError.NOT_FOUND_PRODUCT_BACKLOG)

            val registeredAssignees = assigneeRepository.findAllByProductBacklog(productBacklog)
            registeredAssignees.forEach { assignees ->
                assignees.delete()
            }

            assignMembers(request.assignees, productBacklog)
        }

    private fun assignMembers(
        memberRowidSet: Set<Long>,
        productBacklog: ProductBacklog,
    ) {
        val members = memberRepository.findAllByRowidIn(memberRowidSet.toList())

        if (members.size != memberRowidSet.size) {
            throw BusinessException(BacklogError.NOT_FOUND_ASSIGNEE)
        }

        val assignees =
            members.map { member ->
                Assignee(
                    member = member,
                    productBacklog = productBacklog,
                )
            }

        assigneeRepository.saveAll(assignees)
    }

    fun findAll(projectRowid: Long): List<ApiGetAllProductBacklogResponse> =
        transaction.readOnly {
            val productBacklogList = productBacklogRepository.findAllByProjectRowid(projectRowid)

            val productBacklogsRowidList = productBacklogList.map { it.rowid }.toSet()
            val productBacklogTags = productBacklogTagService.findAllByProductBacklogRowidList(productBacklogsRowidList)

            val tagMap: Map<Long, List<Tag>> =
                productBacklogTags
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
                            tags =
                                tagMap[productBacklog.rowid]
                                    .orEmpty()
                                    .map { tag ->
                                        ApiGetAllProductBacklogResponse.ProductBacklogTag(
                                            productBacklogTagRowid = tag.rowid,
                                            title = tag.title,
                                            color = tag.color,
                                        )
                                    },
                            regDate = productBacklog.regDate,
                        ),
                    ),
                )
            }
        }
}
