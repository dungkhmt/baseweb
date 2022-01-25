package com.hust.baseweb.applications.chat.chattext.controller;

import com.hust.baseweb.applications.chat.chattext.model.ChatMessage;
import com.hust.baseweb.applications.chat.chattext.service.ChatGroupService;
import com.hust.baseweb.applications.chat.chattext.service.MessageService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.UUID;

@Controller
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class ChatSocketController {

    private SimpMessagingTemplate simpMessagingTemplate;
    private ChatGroupService chatGroupService;
    private MessageService messageService;
    private UserService userService;

    @MessageMapping("/text/publicMessage")
    @SendTo("/topic/chat/public")
    public ChatMessage sendMessage(SimpMessageHeaderAccessor sha, @Payload ChatMessage chatMessage) {
        String username = sha.getUser().getName();

        chatMessage.setSender(username);

        return chatMessage;
        ///Save msg
    }

//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public ChatMessage addUser(SimpMessageHeaderAccessor sha, @Payload ChatMessage chatMessage) {
//        String username = sha.getUser().getName();
//
//        chatMessage.setSender(username);
//
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        return chatMessage;
//    }

    @MessageMapping("/text/sendMessageToUser")
    public void sendMessageToUser(SimpMessageHeaderAccessor sha, @Payload ChatMessage chatMessage) {
        String username = sha.getUser().getName();

        chatMessage.setSender(username);

        String destination = chatMessage.getDestination();

        simpMessagingTemplate.convertAndSendToUser(destination, "/queue/messages", chatMessage);
    }

    @MessageMapping("/text/sendMessageToGroup")
    public void sendMessageToGroup(SimpMessageHeaderAccessor sha, @Payload ChatMessage chatMessage) {

        String username = sha.getUser().getName();

        try {
            UserLogin user = userService.findById(username);

            chatMessage.setSender(username);

            chatMessage.setType(ChatMessage.MessageType.GROUP_CHAT);

            String chatGroupId = chatMessage.getDestination();

            if (chatGroupId != null) {
                boolean isUserInGroup = chatGroupService.checkUserInGroup(user.getUserLoginId(), UUID.fromString(chatGroupId));

                if(isUserInGroup) {
                    UUID insertedId = messageService.insertChatMessage(chatMessage);

                    chatMessage.setId(insertedId.toString());

                    List<String> groupMembers = chatGroupService.getAllMembersByChatGroupId(UUID.fromString(chatGroupId));

                    for (String groupMember : groupMembers) {
                        simpMessagingTemplate.convertAndSendToUser(groupMember, "/queue/messages", chatMessage);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
            chatMessage.setType(ChatMessage.MessageType.ERROR);
            chatMessage.setContent(e.getMessage());
            simpMessagingTemplate.convertAndSendToUser(username, "/queue/messages", chatMessage);
        }
    }


}
