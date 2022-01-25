package com.hust.baseweb.applications.chat.websocket;

import com.hust.baseweb.applications.chat.chattext.model.User;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.LinkedList;
import java.util.Map;

public class UserInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);

            Object authorization = ((Map) raw).get("X-Auth-Token");
            Object username = ((Map) raw).get("username");

            String name = "";
            String token = "";

            if (username instanceof LinkedList) {
                name = ((LinkedList<String>) username).get(0).toString();
            }

            if (authorization instanceof LinkedList) {
                token = ((LinkedList<String>) authorization).get(0).toString();
            }

            User user = new User(name);
            user.setToken(token);

            accessor.setUser(user);
        }

        return message;
    }

}
