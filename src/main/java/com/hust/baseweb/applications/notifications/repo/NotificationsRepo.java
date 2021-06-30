package com.hust.baseweb.applications.notifications.repo;

import com.hust.baseweb.applications.notifications.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationsRepo extends JpaRepository<Notifications, UUID> {

    long countByToUserAndStatusId(String toUser, String statusId);
}
