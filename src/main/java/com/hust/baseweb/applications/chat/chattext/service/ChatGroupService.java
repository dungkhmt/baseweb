package com.hust.baseweb.applications.chat.chattext.service;

import com.hust.baseweb.applications.chat.chattext.entity.ChatGroup;
import com.hust.baseweb.applications.chat.chattext.model.ChatGroupModel;
import com.hust.baseweb.rest.user.UserRestBriefProjection;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ChatGroupService {
    List<ChatGroup> getAllChatGroupOfUserLoginId(String userLoginId);

    ChatGroup createChatGroup(ChatGroupModel chatRoomModel);

    List<String> getAllMembersByChatGroupId(UUID chatGroupId);

    Page<UserRestBriefProjection> getGroupMembers(UUID groupId);

    boolean checkUserInGroup(String userLoginId, UUID groupId);

    boolean checkOwner(UUID groupId, String userLoginId);

    int updateChatGroupMembers(UUID groupId, List<String> userLoginIds);
}
