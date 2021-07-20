package com.hust.baseweb.applications.notifications.repo;

import com.hust.baseweb.applications.notifications.entity.Notifications;
import com.hust.baseweb.applications.notifications.entity.QNotifications;
import com.hust.baseweb.applications.notifications.model.NotificationDTO;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;

import java.util.UUID;

public interface NotificationsRepo
    extends JpaRepository<Notifications, UUID>,
    QuerydslPredicateExecutor<Notifications>,
    QuerydslBinderCustomizer<QNotifications> {

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
                   "\tn.from_user = ur.user_login_id where n.to_user = ?1",
           nativeQuery = true)
    Page<NotificationDTO> findAllNotifications(String toUser, Pageable pageable);

    @Override
    default void customize(
        QuerydslBindings bindings, QNotifications root
    ) {
        bindings.bind(String.class)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);

        bindings.excluding(
            root.id,
            root.content,
            root.fromUser,
            root.toUser,
            root.lastUpdatedStamp,
            root.url,
            root.statusId);
    }
}
