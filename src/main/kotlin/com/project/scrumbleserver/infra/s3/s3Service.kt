package com.project.scrumbleserver.infra.s3

import org.springframework.stereotype.Service
import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectResponse
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.io.InputStream
import java.time.Duration

@Service
class s3Service(private val s3Client: S3Client, private val createS3Presigner: S3Presigner) {
    fun uploadFile(bucketName: String, key: String, inputStream: InputStream, contentType: String): String {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .contentType(contentType)
            .build()
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, inputStream.available().toLong()))
        return key
    }

    fun downloadFile(bucketName: String, key: String): ResponseInputStream<GetObjectResponse> {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()

        return s3Client.getObject(getObjectRequest)
    }

    fun deleteFile(bucketName: String, key: String) {
        val deleteObjectRequest = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()
        s3Client.deleteObject(deleteObjectRequest)
    }

    fun generatePresignedPutUrl(
        bucketName: String,
        key: String,
        contentType: String? = null,
        expiration: Duration = Duration.ofHours(1)
    ): String {
        val putObjectRequestBuilder = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)

        contentType?.let { putObjectRequestBuilder.contentType(it) }

        val putObjectRequest = putObjectRequestBuilder.build()

        val presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(expiration)
            .putObjectRequest(putObjectRequest)
            .build()

        val presignedRequest = createS3Presigner.presignPutObject(presignRequest)
        createS3Presigner.close()

        return presignedRequest.url().toString()
    }

}