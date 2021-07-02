package com.hust.baseweb.applications.notifications.repo;

import com.hust.baseweb.applications.notifications.entity.Notifications;
import com.hust.baseweb.applications.notifications.model.NotificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface NotificationsRepo extends JpaRepository<Notifications, UUID> {

    long countByToUserAndStatusId(String toUser, String statusId);

    @Query(value = "select\n" +
                   "\tcast(id as varchar),\n" +
                   "\tcontent,\n" +
                   "\tfrom_user fromUser,\n" +
                   "\turl,\n" +
                   "\tfirst_name firstName,\n" +
                   "\tmiddle_name middleName,\n" +
                   "\tlast_name lastName,\n" +
                   "\tn.status_id statusId,\n" +
                   "\tn.created_stamp createdStamp\n" +
                   "from\n" +
                   "\tnotifications n\n" +
                   "left join user_register ur on\n" +
                   "\tn.from_user = ur.user_login_id",
           nativeQuery = true)
    Page<NotificationDTO> findAllNotifications(Pageable pageable);

    List<Notifications> findByToUserAndStatusId(String toUserId, String currentStatusId);
}
