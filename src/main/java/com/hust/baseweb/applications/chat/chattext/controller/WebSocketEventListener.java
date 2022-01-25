package com.hust.baseweb.applications.chat.chattext.controller;

import com.hust.baseweb.applications.chat.chattext.model.ChatMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.net.ConnectException;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    private SimpMessageSendingOperations messagingTemplate;

    private RedisTemplate redisTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) throws ConnectException{

        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());

//        @SuppressWarnings("rawtypes")
//        GenericMessage connectHeader = (GenericMessage) stompAccessor
//            .getHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER);
//
//        @SuppressWarnings("unchecked")
//        Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) connectHeader.getHeaders()
//                                                                                           .get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
//
//        String name = "";
//        String token = "";
//
//        Object username = nativeHeaders.get("username");
//        if (username instanceof LinkedList) {
//            name = ((LinkedList<String>) username).get(0).toString();
//        }
//
//        Object authorization = nativeHeaders.get("X-Auth-Token");
//        if (authorization instanceof LinkedList) {
//            token = ((LinkedList<String>) authorization).get(0).toString();
//        }
//
//        if(name == "" || token == "" || name != this.getUserNameOfAuthUser(token)) {
//            throw new ConnectException();
//        }
    }

    @EventListener
    public void handleWebSocketConnectedListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());
        @SuppressWarnings("rawtypes")
        GenericMessage connectHeader = (GenericMessage) stompAccessor
                .getHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER);

        @SuppressWarnings("unchecked")
        Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) connectHeader.getHeaders()
                .get(SimpMessageHeaderAccessor.NATIVE_HEADERS);

        String sessionId = stompAccessor.getSessionId();

        logger.info("Chat connection by user <{}> with sessionId <{}>", "", sessionId);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
            logger.info("User Disconnected : " + username);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);

            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }


    private String getUserNameOfAuthUser(String token) {
        if (null == token) {
            return null;
        }

        HashOperations<String, String, SecurityContextImpl> hashOperations = redisTemplate.opsForHash();
        SecurityContextImpl context = hashOperations.get(
            "spring:session:sessions:" + token,
            "sessionAttr:SPRING_SECURITY_CONTEXT");

        if (null == context) {
            return null;
        } else {
            Authentication authentication = context.getAuthentication();

            if (authentication.isAuthenticated()) {
                org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

                return user.getUsername();
            }
        }

        return null;
    }
}
