package com.woniu.yago.exception;

/**
 * @Description: java类作用描述CustomAuthenticationException
 * @Author: lxy
 * @time: 2019/4/23 15:56
 */
public class CustomAuthenticationException extends Throwable {
    // 异常信息
    private String msg;

    public CustomAuthenticationException(String msg){
        super(msg);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
