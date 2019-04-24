package com.woniu.yago.service.impl;

import com.woniu.yago.dao.UserMapper;
import com.woniu.yago.pojo.User;
import com.woniu.yago.service.UserService;
import com.woniu.yago.utils.CodeUtil;
import com.woniu.yago.utils.MailUtil;
import com.woniu.yago.utils.PhoneUtil;
import com.woniu.yago.utils.RegexpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: java类作用描述
 * @Author: 路边
 * @time: 2019/4/16 19:43
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Transactional
    @Override
    public User saveUser(User user) {
        return userMapper.saveUser(user);
    }


    /**
     * 方法实现说明  注册邮箱
     * 1、生成验证码,保存验证码和邮箱，插入成功则通过线程的方式给用户发送一封邮件
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
        if(userMapper.saveUser(user).getUserEmail() != null
                    && userMapper.saveUser(user).getUserVerifyCode()!=null){
            if(userMapper.activeUserByEamil(user.getUserEmail())>0){
                new Thread(new MailUtil(user.getUserEmail(), userVerifyCode,content)).start();
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }

    }

    /**
     * 方法实现说明  手机成功登陆后改变激活状态,激活0--->1
     * @author      lxy
     * @Param:      userVerifyCode
     * @return      boolean true-->更新激活    false--> 更新失败
     * @exception
     * @date        2019/4/17 17:07
     */
    @Override
    public boolean activeUserByPhone(String userPhone) {
        if(userMapper.activeUserByPhone(userPhone)>0){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 方法实现说明
     * @author      lxy 邮箱登录后，激活0--->1
     * @Param:      userEmail
     * @return      boolean true-->更新激活    false--> 更新失败
     * @exception
     * @date        2019/4/22 14:22
     */
    @Override
    public boolean activeUserByEmail(String userEmail) {
        if(userMapper.activeUserByEamil(userEmail)>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public User queryUserByEmail(String userEmail) {
        return userMapper.queryUserByEmail(userEmail);
    }

    @Override
    public User queryUserByEmailAndPwd(String userEmail,String userPwd) {
        return userMapper.queryUserByEmailAndPwd(userEmail,userPwd);
    }

    @Override
    public User queryUserByEmailAndCode(String userEmail, String userVerifyCode) {
        return userMapper.queryUserByEmailAndCode(userEmail,userVerifyCode);
    }

    @Override
    public User queryUserByPhone(String userPhone) {
        return userMapper.queryUserByPhone(userPhone);
    }
    /**
     * 方法实现说明  注册手机时发送密码，
     * 1、查找手机号 2、如果存在，返回false   3、如果不存在，插入手机和密码
     * @author      lxy
     * @Param:
     * @return      boolean
     * @exception
     * @date        2019/4/21 1:53
     */
    @Override
    public boolean sendRegPhonePwd(User user) {
        String userPwd= CodeUtil.userNumber();
        user.setUserPwd(userPwd);
        if(!userMapper.saveUser(user).getUserPhone().equals("") && !userMapper.saveUser(user).getUserPwd().equals("")){
            new PhoneUtil().sendPwd(user.getUserPhone(),userPwd);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public User queryUserByPhoneAndPwd(String userPhone, String userPwd) {
        return userMapper.queryUserByPhoneAndPwd(userPhone,userPwd);
    }
    /**
     * 方法实现说明 登录时，发送验证码，1、查找手机号，无论手机号是否存在，不更新状态
     *              2、手机存在，那么只更新验证码  3、手机不存在，插入手机号和验证码
     *
     * @author      lxy
     * @Param:
     * @return
     * @exception
     * @date        2019/4/21 1:06
     */
    @Override
    public boolean sendLoginMessage(User user) {
        if (user.getUserPhone().equals("")){
            return false;
        }
        if (!user.getUserPhone().matches(RegexpUtil.RegExp_PHONE)){
            return false;
        }
        String userVerifyCode= CodeUtil.userNumber();
        user.setUserVerifyCode(userVerifyCode);
        if (userMapper.queryUserByPhone(user.getUserPhone())!=null){
            if (userMapper.updateCode(userVerifyCode,user.getUserPhone())>0){
                new PhoneUtil().sendCode(user.getUserPhone(),userVerifyCode);
                return true;
            }else {
                return false;

            }
        }else {
            if (!userMapper.saveUser(user).getUserPhone().equals("") && !userMapper.saveUser(user).getUserVerifyCode().equals("")){
                    new PhoneUtil().sendCode(user.getUserPhone(),userVerifyCode);
                    return true;
                }else {
                    return false;
                }
        }

    }

    @Override
    public User queryUserByPhoneAndCode(String userPhone, String userVerifyCode) {
        return userMapper.queryUserByPhoneAndCode(userPhone,userVerifyCode);
    }

}
