package com.caio.pinho.dailyritual.notification.notification;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_notification_history")
public class NotificationHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "co_seq_notification")
	private Long id;

	@Column(name = "co_job", nullable = false, unique = true)
	private Long jobId;

	@Column(name = "co_user", nullable = false)
	private Long userId;

	@Column(name = "co_plan", nullable = false)
	private Long planId;

	@Column(name = "no_title", nullable = false)
	private String title;

	@Column(name = "dt_sent_at", nullable = false)
	private LocalDateTime sentAt;
}
