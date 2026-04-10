package com.caio.pinho.dailyritual.notification.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caio.pinho.dailyritual.notification.dto.NotificationHistoryResponse;
import com.caio.pinho.dailyritual.notification.dto.ReminderDuePayload;
import com.caio.pinho.dailyritual.notification.model.NotificationHistory;
import com.caio.pinho.dailyritual.notification.repository.NotificationHistoryRepository;

@Service
public class NotificationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

	private final NotificationHistoryRepository repository;
	private final Clock clock;

	public NotificationService(NotificationHistoryRepository repository, Clock clock) {
		this.repository = repository;
		this.clock = clock;
	}

	@Transactional
	public void handleReminderDue(ReminderDuePayload payload) {
		if (repository.findByJobId(payload.jobId()).isPresent()) {
			return;
		}

		NotificationHistory history = new NotificationHistory();
		history.setJobId(payload.jobId());
		history.setUserId(payload.userId());
		history.setPlanId(payload.planId());
		history.setTitle(payload.title());
		history.setSentAt(LocalDateTime.now(clock));
		repository.save(history);
		LOGGER.info("Sending reminder for job {} and plan {}", payload.jobId(), payload.planId());
	}

	@Transactional(readOnly = true)
	public List<NotificationHistoryResponse> list(Long userId) {
		return repository.findAllByUserIdOrderBySentAtDesc(userId).stream().map(NotificationHistoryResponse::from).toList();
	}
}
