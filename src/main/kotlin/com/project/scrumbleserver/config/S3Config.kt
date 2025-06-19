package com.project.scrumbleserver.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.net.URI

@Configuration
class S3Config {
    @Value("\${aws.s3.endpoint}")
    private lateinit var endpoint: String

    @Value("\${aws.s3.access-key}")
    private lateinit var accessKey: String

    @Value("\${aws.s3.secret-key}")
    private lateinit var secretKey: String

    @Value("\${aws.s3.region}")
    private lateinit var region: String

    @Bean
    fun s3Client(): S3Client {
        return S3Client.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                )
            )
            .forcePathStyle(true)  // MinIO는 path-style 필요
            .build()
    }
    @Bean
    fun createS3Presigner(): S3Presigner {
        return S3Presigner.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                )
            )
            .build()
    }

}