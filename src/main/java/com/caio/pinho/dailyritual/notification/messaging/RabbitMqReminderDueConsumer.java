package com.caio.pinho.dailyritual.notification.messaging;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.caio.pinho.dailyritual.notification.dto.ReminderDuePayload;
import com.caio.pinho.dailyritual.notification.service.NotificationService;
import com.caio.pinho.dailyritual.shared.messaging.MessageConsumer;

@Component
@ConditionalOnProperty(prefix = "app.messaging", name = "provider", havingValue = "rabbitmq")
public class RabbitMqReminderDueConsumer implements MessageConsumer<String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqReminderDueConsumer.class);
	private static final Pattern JOB_ID_PATTERN = Pattern.compile("\"jobId\":(\\d+)");
	private static final Pattern USER_ID_PATTERN = Pattern.compile("\"userId\":(\\d+)");
	private static final Pattern PLAN_ID_PATTERN = Pattern.compile("\"planId\":(\\d+)");
	private static final Pattern TITLE_PATTERN = Pattern.compile("\"title\":\"([^\"]*)\"");

	private final NotificationService notificationService;

	public RabbitMqReminderDueConsumer(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@RabbitListener(queues = "${app.messaging.reminder-queue:reminder-due}")
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
