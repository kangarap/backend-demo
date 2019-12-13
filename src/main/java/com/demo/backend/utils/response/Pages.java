package com.demo.backend.utils.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class Pages implements Serializable {

    private Object data;
    private int page;
    private int pages;
    private long total;
}
