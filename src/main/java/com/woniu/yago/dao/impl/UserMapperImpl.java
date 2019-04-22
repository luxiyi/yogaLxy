package com.woniu.yago.dao.impl;

import com.woniu.yago.dao.UserMapper;
import com.woniu.yago.pojo.User;
import com.woniu.yago.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @Description: java类作用描述 dao层实现类
 * @Author: 路边
 * @time: 2019/4/16 19:47
 */
@Repository
public class UserMapperImpl implements UserMapper {
    @Autowired
    private UserRepository repository;

    @Override
    public User saveUser(User user) {
        return repository.save(user);
    }


    @Override
    public Integer activeUserByEamil(String userEmail) {
        return repository.activeUserByEmail(userEmail);
    }

    @Override
    public Integer activeUserByPhone(String userPhone) {
        return repository.activeUserByPhone(userPhone);
    }

    @Override
    public User queryUserByEmail(String userEmail) {
        return repository.queryUserByEmail(userEmail);
    }

    @Override
    public User queryUserByEmailAndPwd(String userEmail,String userPwd) {
        return repository.queryUserByEmailAndPwd(userEmail,userPwd);
    }

    @Override
    public User queryUserByPhone(String userPhone) {
        return repository.queryUserByPhone(userPhone);
    }

    @Override
    public User regUserByPhoneAndPwd(String userPhone, String userPwd) {
        return repository.regUserByPhoneAndPwd(userPhone,userPwd);
    }

    @Override
    public User loginByPhoneAndCode(String userPhone, String userVerifyCode) {

        return repository.loginByPhoneAndCode(userPhone,userVerifyCode);
    }

    @Override
    public Integer updateCode(String userVerifyCode,String userPhone) {
        return repository.updateCode(userVerifyCode,userPhone);
    }

    @Override
    public User queryUserByEmailAndCode(String userEmail, String userVerifyCode) {
        return repository.regUserByEmailAndCode(userEmail,userVerifyCode);
    }

//    @Override
//    public User find(String userPhone,Integer active) {
//        return repository.findByUserPhoneAndActive(userPhone,active);
//    }
}
