package com.hust.baseweb.applications.chat.chattext.service;

import com.hust.baseweb.applications.chat.chattext.repo.ChatGroupRepo;
import com.hust.baseweb.applications.chat.chattext.repo.MessageRepo;
import com.hust.baseweb.applications.chat.chattext.entity.Message;
import com.hust.baseweb.applications.chat.chattext.model.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
@Transactional
public class MessageServiceImpl implements MessageService {

    MessageRepo messageRepo;
    ChatGroupRepo chatGroupRepo;

    @Override
    public Page<Message> getMessagePageOfGroupChat(UUID groupId, int page, int limit) {
        Pageable paging = PageRequest.of(page, limit, Sort.by("created_stamp").descending());

        return messageRepo.getMessagePagingByToGroup(groupId, paging);
    }

    @Override
    @Transactional
    public UUID insertChatMessage(ChatMessage chatMessage) {
//        int res = messageRepo.insertMessage(
//            chatMessage.getContent(),
//            chatMessage.getSender(),
//            chatMessage.getType() == ChatMessage.MessageType.PRIVATE_CHAT ? chatMessage.getDestination() : null,
//            chatMessage.getType() == ChatMessage.MessageType.GROUP_CHAT ? UUID.fromString(chatMessage.getDestination()) : null,
//            null
//        );

        Message tmpReply = null;
        if(chatMessage.getReplyTo() != null) {
            tmpReply = new Message();
            tmpReply.setMsgId(UUID.fromString(chatMessage.getReplyTo().getId()));
        }

        Message msg = new Message(
            null,
            chatMessage.getContent(),
            chatMessage.getSender(),
            chatMessage.getType() == ChatMessage.MessageType.PRIVATE_CHAT ? chatMessage.getDestination() : null,
            chatMessage.getType() == ChatMessage.MessageType.GROUP_CHAT ? UUID.fromString(chatMessage.getDestination()) : null,
            tmpReply,
            null,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        messageRepo.saveAndFlush(msg);

        if(chatMessage.getType() == ChatMessage.MessageType.GROUP_CHAT) {
            chatGroupRepo.updateLastMessageTimeByGroupId(UUID.fromString(chatMessage.getDestination()));
        }

        return msg.getMsgId();
    }
}
