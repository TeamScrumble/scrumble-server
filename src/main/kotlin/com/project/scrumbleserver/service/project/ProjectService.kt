package com.project.scrumbleserver.service.project

import com.project.scrumbleserver.api.project.ApiGetAllProjectResponse
import com.project.scrumbleserver.api.project.ApiPostProjectRequest
import com.project.scrumbleserver.api.project.ApiPostProjectResponse
import com.project.scrumbleserver.domain.project.Project
import com.project.scrumbleserver.infra.storage.ImageUploader
import com.project.scrumbleserver.repository.project.ProjectRepository
import com.project.scrumbleserver.global.transaction.Transaction
import com.project.scrumbleserver.service.tag.TagService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ProjectService(
    private val transaction: Transaction,
    private val projectRepository: ProjectRepository,
    private val thumbnailGenerator: ThumbnailGenerator,
    private val imageUploader: ImageUploader,
    private val tagService: TagService,
) {
    fun insert(
        thumbnail: MultipartFile?,
        request: ApiPostProjectRequest,
    ): ApiPostProjectResponse {
        val thumbnailData = thumbnail
            ?.takeIf { !it.isEmpty }
            ?.bytes
            ?: thumbnailGenerator.generate(request.title.getOrNull(0) ?: ' ')

        val thumbnailUrl = imageUploader.upload(thumbnailData)

        val projectRowid = transaction {
            val project = projectRepository.save(Project(
                title = request.title,
                description = request.description ?: "",
                thumbnail = thumbnailUrl
            ))

            tagService.saveBasicTags(project)

            project.rowid
        }


        return ApiPostProjectResponse(projectRowid)
    }

    fun findAll(): ApiGetAllProjectResponse = transaction.readOnly {
        return@readOnly ApiGetAllProjectResponse(
            projectRepository.findAll().map {
                ApiGetAllProjectResponse.Project(
                    rowid = it.rowid,
                    title = it.title,
                    description = it.description,
                    thumbnailUrl = it.thumbnail,
                    regDate = it.regDate
                )
            }
        )
    }
}