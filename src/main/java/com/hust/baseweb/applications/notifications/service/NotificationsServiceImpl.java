package com.hust.baseweb.applications.notifications.service;

import com.hust.baseweb.applications.notifications.entity.Notifications;
import com.hust.baseweb.applications.notifications.repo.NotificationsRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.hust.baseweb.applications.notifications.entity.Notifications.STATUS_CREATED;

@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class NotificationsServiceImpl implements NotificationsService {

    private final NotificationsRepo notificationsRepo;

    @Override
    public Page getNotifications(int page, int size) {
        Pageable sortedByCreatedStampDsc =
            PageRequest.of(page, size, Sort.by("createdStamp").descending());
        Page<Notifications> notifications = notificationsRepo.findAll(sortedByCreatedStampDsc);

        return notifications;
    }

    @Override
    public long countNumUnreadNotification(String toUser) {
        return notificationsRepo.countByToUserAndStatusId(toUser, STATUS_CREATED);
    }
}
