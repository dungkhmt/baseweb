package com.hust.baseweb.applications.chat.chattext.repo;

import com.hust.baseweb.applications.chat.chattext.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MessageRepo extends PagingAndSortingRepository<Message, UUID>, JpaRepository<Message, UUID> {
    @Modifying
    @Query(value = "Insert into message (message, created_by_user_login_id, to_user_login_id, to_group_id, status_id) values (:message, :created_by_user_login_id, :to_user_login_id, :to_group_id, :status_id)", nativeQuery = true)
    int insertMessage(@Param("message")String message, @Param("created_by_user_login_id") String fromUserLoginId, @Param("to_user_login_id") String toUserLoginId, @Param("to_group_id") UUID toGroupId, @Param("status_id") String statusId);

    @Query(value = "select * from message m where to_group_id = :to_group_id", countQuery = "select count(*) from message m",nativeQuery = true)
    Page<Message> getMessagePagingByToGroup(@Param("to_group_id") UUID toGroupId, Pageable pageable);
}
