package com.woniu.yago.utils;

import com.woniu.yago.pojo.Result;

/**
 * @Description: java类作用描述ResultInfo
 * @Author: lxy
 * @time: 2019/4/19 22:57
 */
public class ResultUtil {
    public static final String SUCCESS_INFO = "0";//成功

    public static final String NULL_INFO = "1";//密码或者账号为空

    public static final String EXIST_INFO = "2";//已存在

    public static final String FAIL_INFO = "3";//失败

    //    更新数据库失败
    public static Result connectDatabaseFail() {
        return new Result(0, "服务器连接失败，请稍后再试，或者联系管理员...");
    }

    //    更新数据库成功
    public static Result actionSuccess(String message, Object data) {
        return new Result(1, message, data);
    }
    //    非法操作
    public static Result illegalOperation(String message){
        return new Result(0, "操作非法，请联系管理员...");
    }
    //    错误操作
    public static Result errorOperation(String message){
        return new Result(0, message);
    }
}
