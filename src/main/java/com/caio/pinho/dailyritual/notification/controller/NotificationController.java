package com.caio.pinho.dailyritual.notification.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caio.pinho.dailyritual.notification.dto.NotificationHistoryResponse;
import com.caio.pinho.dailyritual.notification.service.NotificationService;
import com.caio.pinho.dailyritual.shared.security.AuthenticatedUser;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

	private final NotificationService notificationService;

	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@GetMapping
	public List<NotificationHistoryResponse> list(@AuthenticationPrincipal AuthenticatedUser user) {
		return notificationService.list(user.userId());
	}
}
