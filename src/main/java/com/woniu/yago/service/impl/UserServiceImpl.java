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
    private UserMapper mapper;


    @Transactional
    @Override
    public User saveUser(User user) {
        return mapper.saveUser(user);
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
    public boolean regByEmail(User user) {

        String userPwd= CodeUtil.userNumber();
        user.setUserPwd(userPwd);
        if(mapper.saveUser(user).getUserEmail() != null
                && mapper.saveUser(user).getUserPwd()!=null
                    && mapper.saveUser(user).getUserVerifyCode()!=null){
            if(mapper.activeUserByEamil(user.getUserEmail())>0){
                new Thread(new MailUtil(user.getUserEmail(), userPwd)).start();
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
        if(mapper.activeUserByPhone(userPhone)>0){
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
        if(mapper.activeUserByEamil(userEmail)>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public User queryUserByEmail(String userEmail) {
        return mapper.queryUserByEmail(userEmail);
    }

    @Override
    public User loginUserByEmailAndPwd(String userEmail,String userPwd) {
        return mapper.queryUserByEmailAndPwd(userEmail,userPwd);
    }

    @Override
    public User regUserByEmailAndCode(String userEmail, String userVerifyCode) {
        return mapper.queryUserByEmailAndCode(userEmail,userVerifyCode);
    }

    @Override
    public User queryUserByPhone(String userPhone) {
        return mapper.queryUserByPhone(userPhone);
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
    public boolean sendRegPwd(User user) {

        String userPwd= CodeUtil.userNumber();
        user.setUserPwd(userPwd);

        if(!mapper.saveUser(user).getUserPhone().equals("") && !mapper.saveUser(user).getUserPwd().equals("")){
            new PhoneUtil().sendPwd(user.getUserPhone(),userPwd);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public User regUserByPhoneAndPwd(String userPhone, String userPwd) {
        return mapper.regUserByPhoneAndPwd(userPhone,userPwd);
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
        if (mapper.queryUserByPhone(user.getUserPhone())!=null){
            if (mapper.updateCode(userVerifyCode,user.getUserPhone())>0){
                new PhoneUtil().sendCode(user.getUserPhone(),userVerifyCode);
                return true;
            }else {
                return false;

            }
        }else {
            if (!mapper.saveUser(user).getUserPhone().equals("") && !mapper.saveUser(user).getUserVerifyCode().equals("")){
                    new PhoneUtil().sendCode(user.getUserPhone(),userVerifyCode);
                    return true;
                }else {
                    return false;
                }
        }

    }

    @Override
    public User loginByPhoneAndCode(String userPhone, String userVerifyCode) {
        return mapper.loginByPhoneAndCode(userPhone,userVerifyCode);
    }
}
