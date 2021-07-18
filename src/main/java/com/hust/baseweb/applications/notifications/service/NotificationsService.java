package com.hust.baseweb.applications.notifications.service;

import com.hust.baseweb.entity.UserLogin;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface NotificationsService {

    Page getNotifications(UserLogin u, int page, int size);

    long countNumUnreadNotification(String toUser);

    void create(String fromUser, String toUser, String content, String url);

    void updateStatus(UUID notificationId, String status);

    void updateMultipleNotificationsStatus(String userId, String status);
}
