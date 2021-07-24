package com.hust.baseweb.applications.notifications.service;

import com.google.common.collect.Iterables;
import com.hust.baseweb.applications.notifications.entity.Notifications;
import com.hust.baseweb.applications.notifications.entity.QNotifications;
import com.hust.baseweb.applications.notifications.model.NotificationDTO;
import com.hust.baseweb.applications.notifications.repo.NotificationsRepo;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

import static com.hust.baseweb.applications.notifications.entity.Notifications.STATUS_CREATED;
import static com.hust.baseweb.applications.notifications.entity.Notifications.STATUS_READ;

@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class NotificationsServiceImpl implements NotificationsService {

    private final NotificationsRepo notificationsRepo;

    @Override
    public Page getNotifications(String toUser, int page, int size) {
        Pageable sortedByCreatedStampDsc =
            PageRequest.of(page, size, Sort.by("created_stamp").descending());
        Page<NotificationDTO> notifications = notificationsRepo.findAllNotifications(toUser, sortedByCreatedStampDsc);

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
    public void updateMultipleNotificationsStatus(
        String userId,
        String status,
        Date beforeOrAt
    ) {
        QNotifications qNotifications = QNotifications.notifications;
        BooleanExpression unRead = qNotifications.statusId.eq(STATUS_CREATED);
        BooleanExpression toUser = qNotifications.toUser.eq(userId);
        BooleanExpression beforeOrAtTime = qNotifications.createdStamp.loe(beforeOrAt);

        Iterable<Notifications> notifications = notificationsRepo.findAll(toUser.and(unRead).and(beforeOrAtTime));

        // TODO: upgrade this method to check valid status according to notification status transition.
        if (Iterables.size(notifications) > 0) {
            for (Notifications notification : notifications) {
                notification.setStatusId(status);
            }

            notificationsRepo.saveAll(notifications);
        }
    }
}
