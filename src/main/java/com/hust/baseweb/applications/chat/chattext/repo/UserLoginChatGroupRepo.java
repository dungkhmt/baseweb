package com.hust.baseweb.applications.chat.chattext.repo;

import com.hust.baseweb.applications.chat.chattext.entity.UserLoginChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserLoginChatGroupRepo extends JpaRepository<UserLoginChatGroup, UUID> {
    List<UserLoginChatGroup> findAllByGroupId(UUID groupId);

    @Query(value = "select ulgc.group_id from user_login_chat_group ulcg", nativeQuery= true)
    List<UUID> findAllGroupIdByUserLoginId(String userLoginId);

    @Query(value = "select exists (select ulcg.mapping_id from user_login_chat_group ulcg where ulcg.group_id=:group_id AND ulcg.user_login_id=:user_login_id)", nativeQuery= true)
    boolean checkUserLoginInGroup(@Param("user_login_id") String userLoginId, @Param("group_id") UUID groupId);

    @Modifying
    @Query(value = "insert into user_login_chat_group(group_id, user_login_id) SELECT :group_id as group_id, user_login_id FROM user_login WHERE user_login_id IN :user_login_ids",nativeQuery = true)
    int insertUserLoginsToGroup(@Param("group_id") UUID groupId, @Param("user_login_ids") List<String> userLoginIds);

    @Modifying
    @Query(value = "delete from user_login_chat_group WHERE group_id = :group_id AND user_login_id IN :user_login_ids",nativeQuery = true)
    int deleteUserLoginsFromGroup(@Param("group_id") UUID groupId, @Param("user_login_ids") List<String> userLoginIds);
}
