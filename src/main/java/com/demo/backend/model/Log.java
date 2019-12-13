package com.demo.backend.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long logId;
    private long userId;
    private String userName;
    private String logInfo;
    private String method;
    private String params;
    private String ip;
    private long pubTime;
}
