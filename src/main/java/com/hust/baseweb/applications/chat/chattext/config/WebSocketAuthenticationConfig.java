package com.hust.baseweb.applications.chat.chattext.config;

import com.hust.baseweb.applications.chat.chattext.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.LinkedList;
import java.util.Map;

//@Configuration
//@EnableWebSocketMessageBroker
//@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketAuthenticationConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketAuthenticationConfig.class);

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                    MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
                    if(raw instanceof Map) {
                        Object authorization = ((Map) raw).get("X-Auth-Token");
                        Object username = ((Map) raw).get("username");

                        logger.debug("X-Auth-Token: {}", authorization);

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
                }
                return message;
            }
        });
    }
}
