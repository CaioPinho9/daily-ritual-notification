package com.caio.pinho.dailyritual.notification.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfig {

	@Bean
	public SqsClient sqsClient(
			@Value("${app.aws.region:us-east-1}") String region,
			@Value("${app.aws.sqs-endpoint:http://localhost:4566}") String endpoint) {
		return SqsClient.builder()
				.region(Region.of(region))
				.endpointOverride(URI.create(endpoint))
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
				.build();
	}
}
