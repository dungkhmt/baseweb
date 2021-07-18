package com.hust.baseweb.applications.notifications.service;

import com.hust.baseweb.applications.notifications.entity.Notifications;
import com.hust.baseweb.applications.notifications.model.NotificationDTO;
import com.hust.baseweb.applications.notifications.repo.NotificationsRepo;
import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.hust.baseweb.applications.notifications.entity.Notifications.STATUS_CREATED;
import static com.hust.baseweb.applications.notifications.entity.Notifications.STATUS_READ;

@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class NotificationsServiceImpl implements NotificationsService {

    private final NotificationsRepo notificationsRepo;

    @Override
    public Page getNotifications(UserLogin u, int page, int size) {
        Pageable sortedByCreatedStampDsc =
            PageRequest.of(page, size, Sort.by("created_stamp").descending());
        Page<NotificationDTO> notifications = notificationsRepo.findAllNotifications(u.getUserLoginId(),sortedByCreatedStampDsc);

        return notifications;
    }

    @Override
    public long countNumUnreadNotification(String toUser) {
        return notificationsRepo.countByToUserAndStatusId(toUser, STATUS_CREATED);
    }

    @Override
    public void create(String fromUser, String toUser, String content, String url) {
        Notifications notification = new Notifications();

        notification.setFromUser(fromUser);
        notification.setToUser(toUser);
        notification.setContent(content);
        notification.setUrl(url);
        notification.setStatusId(STATUS_CREATED);

        notificationsRepo.save(notification);
    }

    @Override
    public void updateStatus(UUID notificationId, String status) {
        Notifications notification = notificationsRepo.findById(notificationId).orElse(null);

        if (null != notification) {
            notification.setStatusId(STATUS_READ);
            notificationsRepo.save(notification);
        }
    }

    @Override
    public void updateMultipleNotificationsStatus(String userId, String status) {
        List<Notifications> notifications = notificationsRepo.findByToUserAndStatusId(userId, STATUS_CREATED);

        if (null != notifications) {
            for (Notifications notification : notifications) {
                notification.setStatusId(STATUS_READ);
            }

            notificationsRepo.saveAll(notifications);
        }
    }
}
