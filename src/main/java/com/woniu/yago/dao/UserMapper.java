package com.woniu.yago.dao;

import com.woniu.yago.pojo.User;

/**
 * @Description: java类作用描述dao层
 * @Author: 路边
 * @time: 2019/4/16 19:44
 */
public interface UserMapper {

    User saveUser(User user);

    Integer activeUserByEamil(String userEmail);

    Integer activeUserByPhone(String userPhone);

    User queryUserByEmail(String userEmail);

    User queryUserByEmailAndPwd(String userEmail,String userPwd);

    User queryUserByPhone(String userPhone);

    User queryUserByPhoneAndPwd(String userPhone, String userPwd);

    User queryUserByPhoneAndCode(String userPhone, String userVerifyCode);

    Integer updateCode(String userVerifyCode,String userPhone);

    User queryUserByEmailAndCode(String userEmail, String userVerifyCode);


//    User find(String userPhone,Integer active);
}
