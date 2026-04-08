package com.caio.pinho.dailyritual.notification.notification;

public record ReminderDuePayload(Long jobId, Long userId, Long planId, String title) {
}
