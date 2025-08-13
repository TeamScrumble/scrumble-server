package com.project.scrumbleserver.infra.s3

import org.springframework.stereotype.Service
import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.core.async.AsyncResponseTransformer
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectResponse
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectResponse
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import software.amazon.awssdk.transfer.s3.S3TransferManager
import java.io.InputStream
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ForkJoinPool


@Service
class S3Service(
    private val s3AsyncClient: S3AsyncClient,
    private val createS3Presigner: S3Presigner
) {
    // 이거 쓰면 편하게 대용량 파일 병령 업로드 처리 가능
    private final val transferManager: S3TransferManager? = S3TransferManager.builder()
        .s3Client(s3AsyncClient)
        .build()

    fun uploadObject(
        bucketName: String,
        objectName: String,
        inputStream: InputStream,
        contentType: String,
        contentLength: Long
    ): CompletableFuture<PutObjectResponse?> {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(objectName)
            .contentType(contentType)
            .contentLength(contentLength)
            .build()

        val asyncRequestBody = AsyncRequestBody.fromInputStream(
            inputStream,
            contentLength,
            ForkJoinPool.commonPool()
        )

        return s3AsyncClient.putObject(putObjectRequest, asyncRequestBody)
    }


    fun downloadObject(
        bucketName: String,
        objectName: String
    ): CompletableFuture<ResponseInputStream<GetObjectResponse>> {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(objectName)
            .build()

        return s3AsyncClient.getObject(getObjectRequest, AsyncResponseTransformer.toBlockingInputStream())
    }



    /**
     *
     * 향후 코루틴 쓰면 이거 쓰셔도 될 듯
     * 스트림 방식이라 메모리 덜 사용하고 용량이 큰 파일에 적합.
     *
     * **/
//    suspend fun StreamDownload(
//        bucketName: String,
//        key: String
//    ): InputStream {
//        val getObjectRequest = GetObjectRequest.builder()
//            .bucket(bucketName)
//            .key(key)
//            .build()
//
//        val tempFile = Files.createTempFile("s3-download", ".tmp")
//
//        s3AsyncClient.getObject(
//            getObjectRequest,
//            AsyncResponseTransformer.toFile(tempFile)  // 파일시스템에 스트리밍 저장
//        ).await()
//
//        return Files.newInputStream(tempFile).also {
//            tempFile.toFile().deleteOnExit()
//        }
//    }

    fun deleteObject(
        bucketName: String,
        objectName: String
    ) {
        val deleteObjectRequest = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(objectName)
            .build()
        s3AsyncClient.deleteObject(deleteObjectRequest)
    }

    /**
     *
     * presigned 동네임. 서버 통하지 말고 클라가 직접 올리셈.
     *
     * **/

    fun generatePresignedPutUrl(
        bucketName: String,
        objectName: String,
        expiration: Duration = Duration.ofMinutes(3)
    ): String {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(objectName)
            .build()

        val presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(expiration)
            .putObjectRequest(putObjectRequest)
            .build()

        val presignedRequest = createS3Presigner.presignPutObject(presignRequest)

        return presignedRequest.url().toString()
    }

    fun generatePresignedGetUrl(
        bucketName: String,
        objectName: String,
        expiration: Duration = Duration.ofMinutes(3)
    ): String {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(objectName)
            .build()

        val presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(expiration)
            .getObjectRequest(getObjectRequest)
            .build()

        val presignedRequest = createS3Presigner.presignGetObject(presignRequest)

        return presignedRequest.url().toString()
    }

}