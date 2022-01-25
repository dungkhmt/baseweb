package com.hust.baseweb.applications.chat.chattext.repo;

import com.hust.baseweb.applications.chat.chattext.entity.ChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChatGroupRepo extends JpaRepository<ChatGroup, UUID> {
    ChatGroup findChatGroupByGroupId(UUID groupId);

    List<ChatGroup> findAllByCreatedById(String userLoginId);

    List<ChatGroup> findAllByGroupIdIn(List<UUID> groupIds);

    @Modifying
    @Query(value = "Select cg.* from user_login_chat_group ulcg join chat_group cg on cg.group_id = ulcg.group_id where ulcg.user_login_id = :user_login_id order by cg.last_message_time DESC, cg.created_stamp DESC",
           nativeQuery = true)
    List<ChatGroup> findAllByUserLoginId(@Param("user_login_id") String userLoginId);

    @Modifying
    @Query(value = "UPDATE chat_group SET last_message_time = current_timestamp where group_id = :group_id ;",
           nativeQuery = true)
    int updateLastMessageTimeByGroupId(@Param("group_id") UUID groupId);

    @Query(value = "Select ulcg.user_login_id from user_login_chat_group ulcg where ulcg.group_id = :group_id ;",
           nativeQuery = true)
    List<String> findAllUserLoginId(@Param("group_id") UUID groupId);

    @Query(value = "Select exists (select cg.group_id from chat_group cg where cg.group_id = :group_id AND cg.created_by_user_login_id = :created_by_user_login_id);",
           nativeQuery = true)
    boolean checkOwner(@Param("created_by_user_login_id") String userLoginId, @Param("group_id") UUID groupId);
}
