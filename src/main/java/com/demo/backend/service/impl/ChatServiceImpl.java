package com.demo.backend.service.impl;

import com.demo.backend.model.Chat;
import com.demo.backend.model.ChatMessage;
import com.demo.backend.repository.ChatMessageRepository;
import com.demo.backend.repository.ChatRepository;
import com.demo.backend.service.ChatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ChatServiceImpl implements ChatService {

    @Resource
    private ChatRepository chatRepository;

    @Resource
    private ChatMessageRepository chatMessageRepository;

    @Override
    public Page<Chat> findChats(Specification<Chat> specification, Pageable pageable) {


        return chatRepository.findAll(specification, pageable);
    }

    @Override
    public Page<ChatMessage> findChatMessages(Specification<ChatMessage> specification, Pageable pageable) {
        return chatMessageRepository.findAll(specification, pageable);
    }


    @Override
    public Chat findChatByChatId(long chatId) {
        return chatRepository.findById(chatId).orElse(null);
    }

    @Override
    public Chat saveOrUpdateChat(Chat chat) {
        return chatRepository.save(chat);
    }

    @Override
    public Chat findChatByFromAndToAndType(long from, long to, byte type) {
        return chatRepository.findChatByFromUidAndAndToUidAndChatType(from, to, type);
    }

    @Override
    public ChatMessage saveChatMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }


}
