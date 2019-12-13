package com.demo.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@JsonIgnoreProperties(value={"hibernateLazyInitializer"})
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String sn = "";
    private String username = "";
    private String mobile = "";
    private String salt = "";
    private String password = "";
    private String avatar = "";
    private String nickname = "";
    private String token = "";
    private java.sql.Date birthday = java.sql.Date.valueOf("1970-01-01");
    private byte sex;
    private byte level;
    private int regTime;
    private String loginIp = "";
    private int loginTime;
    private int parentId;
    private byte status;
    private byte isDelete;

}
