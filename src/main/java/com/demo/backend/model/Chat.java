package com.demo.backend.model;

import com.demo.backend.utils.TimeUtil;
import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = "p_chat", schema = "perfect", catalog = "")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatId;
    private byte ctype;
    private byte fromUnread;
    private byte toUnread;
    private long pubTime = TimeUtil.getTimeStamp();
    private long updateTime;
    private long fromUid;
    private long toUid;
    private byte chatType;
    private String lastMsg = "";
    private String lastUname = "";


    @Transient
    private Map<String,String> userInfo = new HashMap<>();

}
