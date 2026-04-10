package com.caio.pinho.dailyritual.notification.config;

import java.net.URI;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.caio.pinho.dailyritual.shared.config.AppAwsProperties;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
@ConditionalOnProperty(prefix = "app.messaging", name = "provider", havingValue = "sqs", matchIfMissing = true)
public class SqsConfig {

	@Bean
	public SqsClient sqsClient(AppAwsProperties awsProperties) {
		return SqsClient.builder()
				.region(Region.of(awsProperties.region()))
				.endpointOverride(URI.create(awsProperties.sqsEndpoint()))
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
				.build();
	}
}
