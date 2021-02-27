package com.demo.backend.service;

import com.demo.backend.model.Chat;
import com.demo.backend.model.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ChatService {

    Page<Chat> findChats(Specification<Chat> specification, Pageable pageable);

    Page<ChatMessage> findChatMessages(Specification<ChatMessage> specification, Pageable pageable);

    Chat findChatByChatId(long chatId);

    Chat saveOrUpdateChat(Chat chat);

    Chat findChatByFromAndToAndType(long from, long to, byte type);

    ChatMessage saveChatMessage(ChatMessage chatMessage);
}
