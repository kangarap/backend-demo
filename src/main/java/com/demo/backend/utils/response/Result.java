package com.demo.backend.utils.response;

import lombok.Data;

@Data
public final class Result<T>{

    private int status = 0;

    private String errorCode = "";

    private String errorMsg = "";

    private T resultBody;

    public Result() {
    }

    public Result(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Result(T resultBody) {
        this.resultBody = resultBody;
    }
}
