package com.caio.pinho.dailyritual.notification.notification;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {
	Optional<NotificationHistory> findByJobId(Long jobId);
	List<NotificationHistory> findAllByUserIdOrderBySentAtDesc(Long userId);
}
