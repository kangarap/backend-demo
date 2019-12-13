package com.demo.backend.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "admin_user")
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short userId;
    private String realname = "";
    private String username = "";
    private String mobile = "";
    private String password = "";
    private Long addTime;
    private int lastLogin;
    private String lastIp = "";
    private short roleId;
    private byte status;
    private byte isDelete;
}
