package com.demo.backend.esModel;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@Document(indexName = "user")
public class UserEs implements Serializable {

    @Id
    private int userId;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String username;

    @Field(type = FieldType.Text)
    private String mobile;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String nickname;

    @Field(type = FieldType.Byte)
    private byte status;

    @Field(type = FieldType.Integer)
    private int regTime;
}
