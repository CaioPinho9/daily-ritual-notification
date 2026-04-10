package com.caio.pinho.dailyritual.notification.messaging;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.caio.pinho.dailyritual.notification.dto.ReminderDuePayload;
import com.caio.pinho.dailyritual.notification.service.NotificationService;
import com.caio.pinho.dailyritual.shared.config.AppSqsProperties;
import com.caio.pinho.dailyritual.shared.messaging.MessageConsumer;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Component
@ConditionalOnProperty(prefix = "app.messaging", name = "provider", havingValue = "sqs", matchIfMissing = true)
public class SqsReminderDueConsumer implements MessageConsumer<String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SqsReminderDueConsumer.class);
	private static final Pattern JOB_ID_PATTERN = Pattern.compile("\"jobId\":(\\d+)");
	private static final Pattern USER_ID_PATTERN = Pattern.compile("\"userId\":(\\d+)");
	private static final Pattern PLAN_ID_PATTERN = Pattern.compile("\"planId\":(\\d+)");
	private static final Pattern TITLE_PATTERN = Pattern.compile("\"title\":\"([^\"]*)\"");

	private final SqsClient sqsClient;
	private final NotificationService notificationService;
	private final String queueUrl;

	public SqsReminderDueConsumer(
			SqsClient sqsClient,
			NotificationService notificationService,
			AppSqsProperties sqsProperties) {
		this.sqsClient = sqsClient;
		this.notificationService = notificationService;
		this.queueUrl = sqsProperties.reminderQueueUrl();
	}

	@Scheduled(fixedDelay = 5000)
	public void poll() {
		try {
			for (Message message : sqsClient.receiveMessage(
					ReceiveMessageRequest.builder().queueUrl(queueUrl).maxNumberOfMessages(10).waitTimeSeconds(1).build()).messages()) {
				consume(message.body());
				sqsClient.deleteMessage(DeleteMessageRequest.builder().queueUrl(queueUrl).receiptHandle(message.receiptHandle()).build());
			}
		}
		catch (Exception exception) {
			LOGGER.debug("Skipping SQS poll for notification consumer", exception);
		}
	}

	@Override
	public void consume(String body) {
		try {
			notificationService.handleReminderDue(new ReminderDuePayload(
					Long.parseLong(extract(JOB_ID_PATTERN, body)),
					Long.parseLong(extract(USER_ID_PATTERN, body)),
					Long.parseLong(extract(PLAN_ID_PATTERN, body)),
					extract(TITLE_PATTERN, body)));
		}
		catch (IllegalArgumentException exception) {
			LOGGER.warn("Invalid ReminderDue message", exception);
		}
	}

	private String extract(Pattern pattern, String body) {
		Matcher matcher = pattern.matcher(body);
		if (!matcher.find()) {
			throw new IllegalArgumentException("Missing field in message body");
		}
		return matcher.group(1);
	}
}
