package com.hust.baseweb.applications.chat.chattext.service;

import com.hust.baseweb.applications.chat.chattext.repo.ChatGroupRepo;
import com.hust.baseweb.applications.chat.chattext.repo.UserLoginChatGroupRepo;
import com.hust.baseweb.applications.chat.chattext.entity.ChatGroup;
import com.hust.baseweb.applications.chat.chattext.entity.UserLoginChatGroup;
import com.hust.baseweb.applications.chat.chattext.model.ChatGroupModel;
import com.hust.baseweb.repo.UserLoginRepo;
import com.hust.baseweb.rest.user.UserRestBriefProjection;
import com.hust.baseweb.rest.user.UserRestRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
@Transactional
@org.springframework.transaction.annotation.Transactional
public class ChatGroupServiceImpl implements ChatGroupService {

    public static final String module = ChatGroupServiceImpl.class.getName();

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(
        ChatGroupServiceImpl.class);

    private UserLoginRepo userLoginRepo;
    private ChatGroupRepo chatGroupRepo;
    private UserLoginChatGroupRepo userLoginChatGroupRepo;
    private UserRestRepository userRestRepository;

    public List<String> getAllMembersByChatGroupId(UUID chatGroupId) {
        return userLoginChatGroupRepo
            .findAllByGroupId(chatGroupId)
            .stream()
            .map(p -> p.getUserLoginId())
            .collect(Collectors.toList());
    }

    @Override
    public List<ChatGroup> getAllChatGroupOfUserLoginId(String userLoginId) {
        List<ChatGroup> chatGroups = chatGroupRepo.findAllByUserLoginId(userLoginId);
        return chatGroups;
    }

    @Override
    public ChatGroup createChatGroup(ChatGroupModel chatRoomModel) {
        ChatGroup chatGroup = new ChatGroup();
        chatGroup.setGroupName(chatRoomModel.getName());
        chatGroup.setCreatedById(chatRoomModel.getOwnerId());

        chatGroup = chatGroupRepo.saveAndFlush(chatGroup);

        List<UserLoginChatGroup> ulcgs = new ArrayList<>();

        for (String memberId : chatRoomModel.getMemberIds()) {
            UserLoginChatGroup ulcg = new UserLoginChatGroup();
            ulcg.setGroupId(chatGroup.getGroupId());
            ulcg.setUserLoginId(memberId);

            ulcgs.add(ulcg);
        }

        userLoginChatGroupRepo.saveAll(ulcgs);

        return chatGroup;
    }

    @Override
    public Page<UserRestBriefProjection> getGroupMembers(UUID groupId) {
        List<String> userLoginIds = chatGroupRepo.findAllUserLoginId(groupId);

        Page<UserRestBriefProjection> users = userRestRepository.findByLoginUserIds(Pageable.unpaged(), userLoginIds);

        return users;
    }

    @Override
    public boolean checkUserInGroup(String userLoginId, UUID groupId) {
        return userLoginChatGroupRepo.checkUserLoginInGroup(userLoginId, groupId);
    }

    @Override
    public boolean checkOwner(UUID groupId, String userLoginId) {
        return chatGroupRepo.checkOwner(userLoginId, groupId);
    }

    @Override
    public int updateChatGroupMembers(UUID groupId, List<String> userLoginIds) {
        int inserted = 0;
        int removed = 0;

        List<String> members = getAllMembersByChatGroupId(groupId);

        List<String> newMembers = userLoginIds.stream().filter(m -> !members.contains(m)).collect(Collectors.toList());;
        List<String> removedMembers = members.stream().filter(m -> !userLoginIds.contains(m)).collect(Collectors.toList());;


        if(newMembers.size() > 0) {
            inserted = userLoginChatGroupRepo.insertUserLoginsToGroup(groupId, newMembers);
        }
        if(removedMembers.size() > 0) {
            removed = userLoginChatGroupRepo.deleteUserLoginsFromGroup(groupId, removedMembers);
        }

        return inserted + removed;
    }
}
