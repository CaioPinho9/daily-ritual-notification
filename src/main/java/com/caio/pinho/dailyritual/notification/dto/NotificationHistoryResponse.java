package com.caio.pinho.dailyritual.notification.dto;

import java.time.LocalDateTime;

import com.caio.pinho.dailyritual.notification.model.NotificationHistory;

public record NotificationHistoryResponse(Long id, Long jobId, String title, LocalDateTime sentAt) {
	public static NotificationHistoryResponse from(NotificationHistory history) {
		return new NotificationHistoryResponse(history.getId(), history.getJobId(), history.getTitle(), history.getSentAt());
	}
}
