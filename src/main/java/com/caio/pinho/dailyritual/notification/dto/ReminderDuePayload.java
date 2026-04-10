package com.caio.pinho.dailyritual.notification.dto;

public record ReminderDuePayload(Long jobId, Long userId, Long planId, String title) {
}
