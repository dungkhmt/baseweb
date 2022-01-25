package com.hust.baseweb.applications.chat.chattext.controller;

import com.hust.baseweb.applications.chat.chattext.entity.ChatGroup;
import com.hust.baseweb.applications.chat.chattext.model.ChatGroupModel;
import com.hust.baseweb.applications.chat.chattext.service.ChatGroupService;
import com.hust.baseweb.applications.chat.chattext.entity.Message;
import com.hust.baseweb.applications.chat.chattext.service.MessageService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.rest.user.UserRestBriefProjection;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class ChatAPIController {
    public static final String module = ChatAPIController.class.getName();

    private ChatGroupService chatGroupService;
    private UserService userService;
    private MessageService messageService;

    @GetMapping("/chat-group/me")
    public ResponseEntity<List<ChatGroup>> getAllOrders(Principal principal) {
        UserLogin u = userService.findById(principal.getName());
        return ResponseEntity.ok().body(chatGroupService.getAllChatGroupOfUserLoginId(u.getUserLoginId()));
    }

    @PostMapping("/chat-group")
    public ResponseEntity<ChatGroup> createChatRoom(@RequestBody ChatGroupModel chatRoomModel, Principal principal) {
        UserLogin u = userService.findById(principal.getName());

        chatRoomModel.setOwnerId(u.getUserLoginId());

        ChatGroup res = chatGroupService.createChatGroup(chatRoomModel);

        return ResponseEntity.ok().body(res);
    }

    @PutMapping("/chat-group/members/{groupId}")
    public ResponseEntity<Integer> updateChatRoom(@RequestBody List<String> userLoginIds, @PathVariable UUID groupId, Principal principal) {
        UserLogin u = userService.findById(principal.getName());

        if(!chatGroupService.checkOwner(groupId, u.getUserLoginId())) {
            return ResponseEntity.status(401).body(null);
        };

        int res = chatGroupService.updateChatGroupMembers(groupId, userLoginIds);

        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/chat-group/messages")
    public ResponseEntity<Page<Message>> getAllMessagesOfChatRoom(Principal principal, @RequestParam UUID groupId, @RequestParam int page, @RequestParam int limit) {
        UserLogin u = userService.findById(principal.getName());

        List<String> groupMembers = chatGroupService.getAllMembersByChatGroupId(groupId);

        if(!groupMembers.contains(u.getUserLoginId())) {
            return ResponseEntity.status(401).body(null);
        }

        Page<Message> messageList = messageService.getMessagePageOfGroupChat(groupId, page, limit);

        return ResponseEntity.ok().body(messageList);
    }

    @GetMapping("/chat-group/members")
    public ResponseEntity<Page<UserRestBriefProjection>> getAllMembersOfChatRoom(Principal principal, @RequestParam UUID groupId) {
        UserLogin u = userService.findById(principal.getName());

        List<String> groupMembers = chatGroupService.getAllMembersByChatGroupId(groupId);

        if(!groupMembers.contains(u.getUserLoginId())) {
            return ResponseEntity.status(401).body(null);
        }

        Page<UserRestBriefProjection> users = chatGroupService.getGroupMembers(groupId);

        return ResponseEntity.ok().body(users);
    }
}
