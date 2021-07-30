package com.hust.baseweb.applications.notifications.service;

import com.hust.baseweb.applications.notifications.model.NotificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface NotificationsService {

    String SSE_EVENT_HEARTBEAT = "HEARTBEAT";

    String SSE_EVENT_NEW_NOTIFICATION = "NEW_NOTIFICATION";

    // Use concurrent instead of synchronized collection because of performance and thread-safe
    ConcurrentHashMap<String, SseEmitter> subscriptions = new ConcurrentHashMap<>();

    Page<NotificationDTO> getNotifications(String toUser, int page, int size);

    long countNumUnreadNotification(String toUser);

    void create(String fromUser, String toUser, String content, String url);

    void updateStatus(UUID notificationId, String status);

    void updateMultipleNotificationsStatus(String userId, String status, Date beforeOrAt);
}
