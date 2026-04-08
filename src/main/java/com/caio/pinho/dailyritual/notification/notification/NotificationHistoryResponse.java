package com.caio.pinho.dailyritual.notification.notification;

import java.time.LocalDateTime;

public record NotificationHistoryResponse(Long id, Long jobId, String title, LocalDateTime sentAt) {
	static NotificationHistoryResponse from(NotificationHistory history) {
		return new NotificationHistoryResponse(history.getId(), history.getJobId(), history.getTitle(), history.getSentAt());
	}
}
