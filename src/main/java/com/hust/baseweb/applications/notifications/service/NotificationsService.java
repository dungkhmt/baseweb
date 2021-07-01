package com.hust.baseweb.applications.notifications.service;

import org.springframework.data.domain.Page;

import java.util.UUID;

public interface NotificationsService {

    Page getNotifications(int page, int size);

    long countNumUnreadNotification(String toUser);

    void create(String fromUser, String toUser, String content, String url);

    void markAsRead(UUID notificationId);
}
