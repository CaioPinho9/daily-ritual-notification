package com.caio.pinho.dailyritual.notification.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class NotificationServiceTest {

	@Autowired
	private NotificationService notificationService;

	@Test
	void shouldDeduplicateByJobId() {
		ReminderDuePayload payload = new ReminderDuePayload(7L, 3L, 9L, "Review");
		notificationService.handleReminderDue(payload);
		notificationService.handleReminderDue(payload);

		assertEquals(1, notificationService.list(3L).size());
	}

	@Test
	void shouldListOnlyNotificationsForRequestedUser() {
		notificationService.handleReminderDue(new ReminderDuePayload(7L, 3L, 9L, "Review"));
		notificationService.handleReminderDue(new ReminderDuePayload(8L, 4L, 10L, "Practice"));

		assertEquals(1, notificationService.list(3L).size());
		assertEquals("Review", notificationService.list(3L).get(0).title());
	}
}
