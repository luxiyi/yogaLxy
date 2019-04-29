package com.woniu.yago.service;

import com.woniu.yago.pojo.User;

/**
 * @Description: java类作用描述
 * @Author: 路边
 * @time: 2019/4/16 10:25
 */
public interface UserService {
    //插入
    void saveUser(User user);
    //邮箱注册验证
    boolean sendRegEmailCode(User user);
    //查询邮箱是否存在
    User queryUserByEmail(String userEmail);
    //查询手机是否存在
    User queryUserByPhone(String userPhone);
    //注册，登录发送验证码（密码），并插入redis
    boolean sendPhoneMessage(User user,Integer templateId);
    //登录注册手机，激活状态
//    boolean activeUserByPhone(String userPhone);
    //登录注册邮箱，激活状态
//    boolean activeUserByEmail(String userEmail);

    //邮箱登录，查询邮箱和密码
//    User queryUserByEmailAndPwd(String userEmail,String userPwd);
    //邮箱注册，查询邮箱和验证码
//    User queryUserByEmailAndCode(String userEmail, String userVerifyCode);

    //已发送验证码，注册手机，查询用户名
//    User queryUserByPhoneAndPwd(String userPhone, String userPwd);
    //登录发送验证码
//    boolean sendLoginMessage(User user);
    //登录手机，查询验证手机和验证码
//    User queryUserByPhoneAndCode(String userPhone, String userVerifyCode);



}
