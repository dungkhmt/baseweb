package com.hust.baseweb.applications.chat.chattext.service;

import com.hust.baseweb.applications.chat.chattext.model.ChatMessage;
import com.hust.baseweb.applications.chat.chattext.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface MessageService {
    Page<Message> getMessagePageOfGroupChat(UUID groupId, int page, int limit);

    UUID insertChatMessage(ChatMessage chatMessage);
}
