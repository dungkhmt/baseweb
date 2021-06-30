package com.hust.baseweb.applications.notifications.service;

import org.springframework.data.domain.Page;

public interface NotificationsService {

    Page getNotifications(int page, int size);

    long countNumUnreadNotification(String toUser);
}
