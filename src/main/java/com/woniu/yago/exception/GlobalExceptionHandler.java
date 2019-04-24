package com.woniu.yago.exception;

import com.woniu.yago.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: java类作用描述GlobalExceptionHandler
 * 用于捕获和处理Controller抛出的异常
 * @Author: lxy
 * @time: 2019/4/23 16:00
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomAuthenticationException.class)
    @ResponseBody
    public String handleAuthentication(Exception ex){
        LOG.info("Authentication Exception handler  " + ex.getMessage() );
        return ResultUtil.illegalOperation(ex.getMessage()).getMessage();
    }
}
