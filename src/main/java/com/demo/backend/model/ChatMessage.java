package com.demo.backend.model;

import com.demo.backend.utils.TimeUtil;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "p_chat_message", schema = "perfect", catalog = "")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long messageId;
    private long chatId;
    private byte mtype;
    private String content = "";
    private long pubTime = TimeUtil.getTimeStamp();
    private long fromUid;
    private long toUid;
    private byte isAdmin = 0;

}
