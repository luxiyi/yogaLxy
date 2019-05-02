package com.woniu.yago.service.impl;

import com.woniu.yago.pojo.User;
import com.woniu.yago.repository.UserRepository;
import com.woniu.yago.service.UserService;
import com.woniu.yago.utils.CodeUtil;
import com.woniu.yago.utils.MailUtil;
import com.woniu.yago.utils.PhoneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @Description: java类作用描述
 * @Author: 路边
 * @time: 2019/4/16 19:43
 */
@Service
@Repository
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Transactional
    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }


    /**
     * 方法实现说明  发送注册邮箱的验证码
     * 1、生成验证码,保存验证码和邮箱，插入到redis中，键为邮箱，值为验证码
     * 2、在redis中，查找邮箱是否存在，存在代表插入成功，为true，此时不能更改激活状态，激活码改变在注册成功改变
     * 3、成功，则通过线程的方式给用户发送一封邮件，并且发送的时候在Redis设置验证码过期时间
     *
     * 2、
     * @author      lxy
     * @Param:      userName,email,password,code,
     * @return      boolean true-->注册成功 false-->注册失败
     * @exception
     * @date        2019/4/17 17:03
     */
    @Override
    public boolean sendRegEmailCode(User user) {
        String userVerifyCode= CodeUtil.userNumber();
        user.setUserVerifyCode(userVerifyCode);
        String content = "<html><head></head><body><h1>这是一封绝密邮件,不要随便将内容透露给别人。" +
                "</h1><br><h3>您本次注册的所需验证码为：" + user.getUserVerifyCode() + "。请尽快注册，验证码有效时间为3分钟，超出时间范围内，需重新获取。</h3></body></html>";
        stringRedisTemplate.opsForValue().set(user.getUserEmail(),user.getUserVerifyCode());
        if(stringRedisTemplate.hasKey(user.getUserEmail())){
                new Thread(new MailUtil(user.getUserEmail(), userVerifyCode,content)).start();
                stringRedisTemplate.expire(user.getUserEmail(),100, TimeUnit.SECONDS);
                return true;
            }else {
                return false;
        }
    }

    @Override
    public User queryUserByEmail(String userEmail) {
        return userRepository.queryUserByEmail(userEmail);
    }

    @Override
    public User queryUserByPhone(String userPhone) {
        return userRepository.queryUserByPhone(userPhone);
    }

    /**
     * 方法实现说明  登录和注册手机时发送密码（验证码）
     * 1、生成验证码,保存密码（验证码）和手机，插入到redis中，键为邮箱，值为密码（验证码）
     * 2、在redis中，查找邮箱是否存在，存在代表插入成功，为true，此时不能更改激活状态，激活码改变在注册成功改变
     * 3、成功，则通过线程的方式给用户发送短信，并且发送的时候在Redis设置密码（验证码）过期时间
     * @author      lxy
     * @Param:
     * @return      boolean
     * @exception
     * @date        2019/4/21 1:53
     */
    @Override
    public boolean sendPhoneMessage(User user,Integer templateId) {
        String userPwd= CodeUtil.userNumber();
        user.setUserPwd(userPwd);
        stringRedisTemplate.opsForValue().set(user.getUserPhone(),userPwd);
        if(!stringRedisTemplate.hasKey(user.getUserPhone())){
            return false;
        }
        new PhoneUtil(templateId).sendPhoneMessage(user.getUserPhone(),userPwd);
        stringRedisTemplate.expire(user.getUserPhone(),100,TimeUnit.SECONDS);
        return true;

    }

    /**
     * 方法实现说明  手机成功登陆后改变激活状态,激活0--->1
     * @author      lxy
     * @Param:      userVerifyCode
     * @return      boolean true-->更新激活    false--> 更新失败
     * @exception
     * @date        2019/4/17 17:07
     */
//    @Override
//    public boolean activeUserByPhone(String userPhone) {
//        if(userMapper.activeUserByPhone(userPhone)>0){
//            return true;
//        }else{
//            return false;
//        }
//    }
    /**
     * 方法实现说明
     * @author      lxy 邮箱登录后，激活0--->1
     * @Param:      userEmail
     * @return      boolean true-->更新激活    false--> 更新失败
     * @exception
     * @date        2019/4/22 14:22
     */
//    @Override
//    public boolean activeUserByEmail(String userEmail) {
//        if(userMapper.activeUserByEamil(userEmail)>0){
//            return true;
//        }else{
//            return false;
//        }
//    }


//    @Override
//    public User queryUserByEmailAndPwd(String userEmail,String userPwd) {
//        return userMapper.queryUserByEmailAndPwd(userEmail,userPwd);
//    }

//    @Override
//    public User queryUserByEmailAndCode(String userEmail, String userVerifyCode) {
//        return userMapper.queryUserByEmailAndCode(userEmail,userVerifyCode);
//    }




//    @Override
//    public User queryUserByPhoneAndPwd(String userPhone, String userPwd) {
//        return userMapper.queryUserByPhoneAndPwd(userPhone,userPwd);
//    }

//
//    @Override
//    public User queryUserByPhoneAndCode(String userPhone, String userVerifyCode) {
//        return userMapper.queryUserByPhoneAndCode(userPhone,userVerifyCode);
//    }



}
