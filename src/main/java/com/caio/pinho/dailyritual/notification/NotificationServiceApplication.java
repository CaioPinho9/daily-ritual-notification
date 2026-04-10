package com.caio.pinho.dailyritual.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.caio.pinho.dailyritual.shared.api.CommonApiExceptionHandler;
import com.caio.pinho.dailyritual.shared.api.HealthController;
import com.caio.pinho.dailyritual.shared.config.AppAwsProperties;
import com.caio.pinho.dailyritual.shared.config.AppMessagingProperties;
import com.caio.pinho.dailyritual.shared.config.AppSqsProperties;
import com.caio.pinho.dailyritual.shared.security.SharedSecurityConfiguration;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({ AppMessagingProperties.class, AppAwsProperties.class, AppSqsProperties.class })
@Import({ SharedSecurityConfiguration.class, HealthController.class, CommonApiExceptionHandler.class })
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}
}
