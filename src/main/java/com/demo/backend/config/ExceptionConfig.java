package com.demo.backend.config;

import com.demo.backend.utils.response.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionConfig {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<String> errorHandler(HttpServletRequest request, Exception e)
    {
        Result<String> result = new Result<>();
        e.printStackTrace();
        result.setErrorMsg("操作失败");
        result.setErrorCode("1");
        return  result;
    }

}
