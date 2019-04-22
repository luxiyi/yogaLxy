package com.woniu.yago.service;

import com.woniu.yago.pojo.User;

/**
 * @Description: java类作用描述
 * @Author: 路边
 * @time: 2019/4/16 10:25
 */
public interface UserService {
    User saveUser(User user);
    //邮箱注册验证
    boolean regByEmail(User user);
    //登录注册手机，激活状态
    boolean activeUserByPhone(String userPhone);
    //登录注册邮箱，激活状态
    boolean activeUserByEmail(String userEmail);
    //查询邮箱是否存在
    User queryUserByEmail(String userEmail);
    //邮箱登录，查询邮箱和密码
    User loginUserByEmailAndPwd(String userEmail,String userPwd);
    //邮箱注册，查询邮箱和验证码
    User regUserByEmailAndCode(String userEmail, String userVerifyCode);
    //查询手机是否存在
    User queryUserByPhone(String userPhone);
    //注册发送验证码，并插入
    boolean sendRegPwd(User user);
    //已发送验证码，注册手机，查询用户名
    User regUserByPhoneAndPwd(String userPhone, String userPwd);
    //登录发送验证码
    boolean sendLoginMessage(User user);
    //登录手机，查询验证手机和验证码
    User loginByPhoneAndCode(String userPhone, String userVerifyCode);

}
