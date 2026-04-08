package com.caio.pinho.dailyritual.notification.notification;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Component
public class SqsReminderDueConsumer {

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
			@Value("${app.sqs.reminder-queue-url:http://localhost:4566/000000000000/reminder-due}") String queueUrl) {
		this.sqsClient = sqsClient;
		this.notificationService = notificationService;
		this.queueUrl = queueUrl;
	}

	@Scheduled(fixedDelay = 5000)
	public void poll() {
		try {
			for (Message message : sqsClient.receiveMessage(
					ReceiveMessageRequest.builder().queueUrl(queueUrl).maxNumberOfMessages(10).waitTimeSeconds(1).build()).messages()) {
				process(message);
			}
		}
		catch (Exception exception) {
			LOGGER.debug("Skipping SQS poll for notification consumer", exception);
		}
	}

	private void process(Message message) {
		try {
			String body = message.body();
			notificationService.handleReminderDue(new ReminderDuePayload(
					Long.parseLong(extract(JOB_ID_PATTERN, body)),
					Long.parseLong(extract(USER_ID_PATTERN, body)),
					Long.parseLong(extract(PLAN_ID_PATTERN, body)),
					extract(TITLE_PATTERN, body)));
			sqsClient.deleteMessage(DeleteMessageRequest.builder().queueUrl(queueUrl).receiptHandle(message.receiptHandle()).build());
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
