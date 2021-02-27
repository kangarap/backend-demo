package com.demo.backend.repository;

import com.demo.backend.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ChatRepository extends JpaRepository<Chat, Long>, JpaSpecificationExecutor<Chat> {

    @Query("SELECT c from Chat c where (c.fromUid = :from and c.toUid = :to) or (c.fromUid = :to and c.toUid = :from) and c.chatType = :type")
    Chat findChatByFromUidAndAndToUidAndChatType(long from, long to, byte type);
}
