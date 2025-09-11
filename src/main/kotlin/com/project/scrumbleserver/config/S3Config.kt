package com.project.scrumbleserver.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.net.URI


@Configuration
@EnableConfigurationProperties(S3Properties::class)
class S3Config{
    @Bean
    fun createS3AsyncClient(s3Properties: S3Properties): S3AsyncClient {
        return S3AsyncClient.builder()
            .endpointOverride(URI.create(s3Properties.endpoint))
            .region(Region.of(s3Properties.region))
            .credentialsProvider(
                createStaticCredentialsProvider(s3Properties)
            )
            .multipartEnabled(true)  // multipart 받을 수 있도록
            .forcePathStyle(true)  // MinIO는 path-style 필요
            .build()
    }

    @Bean
    fun createS3Client(s3Properties: S3Properties): S3Client {
        return S3Client.builder()
            .endpointOverride(URI.create(s3Properties.endpoint))
            .region(Region.of(s3Properties.region))
            .credentialsProvider(
                createStaticCredentialsProvider(s3Properties)
            )
            .forcePathStyle(true)  // MinIO는 path-style 필요
            .build()
    }

    @Bean
    fun createS3Presigner(s3Properties: S3Properties): S3Presigner {
        return S3Presigner.builder()
            .endpointOverride(URI.create(s3Properties.endpoint))
            .region(Region.of(s3Properties.region))
            .credentialsProvider(
                createStaticCredentialsProvider(s3Properties)
            )
            .build()
    }

    private fun createStaticCredentialsProvider(s3Properties: S3Properties): StaticCredentialsProvider = StaticCredentialsProvider.create(
            AwsBasicCredentials.create(s3Properties.accessKey, s3Properties.secretKey)
        )
    }

@ConfigurationProperties("aws.s3")
data class S3Properties(
    val endpoint: String,
    val accessKey: String,
    val secretKey: String,
    val region: String,
)