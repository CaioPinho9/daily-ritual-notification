package com.caio.pinho.dailyritual.notification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caio.pinho.dailyritual.notification.model.NotificationHistory;

public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {
	Optional<NotificationHistory> findByJobId(Long jobId);
	List<NotificationHistory> findAllByUserIdOrderBySentAtDesc(Long userId);
}
