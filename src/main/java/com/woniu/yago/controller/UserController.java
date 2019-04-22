package com.woniu.yago.controller;

import com.woniu.yago.pojo.User;
import com.woniu.yago.service.UserService;
import com.woniu.yago.utils.MailUtil;
import com.woniu.yago.utils.PhoneUtil;
import com.woniu.yago.utils.RegexpUtil;
import com.woniu.yago.utils.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Description: java类作用描述
 * 用户填写相关信息，点击注册按钮
 * 系统先将用户记录保存到数据库中，其中用户状态为未激活
 * 系统发送一封邮件并通知用户去验证
 * 用户登录邮箱并点击激活链接
 * 系统将用户状态更改为已激活并通知用户注册成功
 * @Author: 路边
 * @time: 2019/4/16 10:25
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    //-----------------------------------邮箱方式-----------------------------

    /**
     * 方法实现说明 邮箱发送密码邮件到email
     * @author      lxy
     * @Param:      email，password，userName
     * @return      json String "success" "fail"
     * @exception
     * @date        2019/4/17 16:26
     */
    @RequestMapping(value = "/sendRegEmailPwd")
    @ResponseBody
    public String sendRegEmailPwd(String userEmail,User user){
        if(userEmail.equals("")){
            return ResultInfo.NULL_INFO;
        }
        if(!user.getUserEmail().matches(RegexpUtil.RegExp_Mail)) {
            return ResultInfo.FAIL_INFO;
        }
        if (userService.queryUserByEmail(userEmail)!=null){
            return ResultInfo.EXIST_INFO;
        }
        user.setUserEmail(userEmail);
        if (userService.regByEmail(user)){
            return ResultInfo.SUCCESS_INFO;
        }else {
            return ResultInfo.FAIL_INFO;
        }
    }
    /**
     * 方法实现说明 注册邮箱，从邮箱中获取验证码，输入密码，验证密码和邮箱、验证码，
     *
     * @author      lxy
     * @Param:      userVerifyCode，userPhone,activa
     * @return      json String "0","1","3"
     * @exception
     * @date        2019/4/19 17:22
     */
    @RequestMapping("regByEmail")
    @ResponseBody
    public String regByEmail(String userEmail,String userPwd,String userVerifyCode,User user,HttpSession session){
        if (userPwd.equals("") || userEmail.equals("") || userVerifyCode.equals("")){
            return ResultInfo.NULL_INFO;
        }
        if(!user.getUserEmail().matches(RegexpUtil.RegExp_Mail)) {
            return ResultInfo.FAIL_INFO;
        }
        user=userService.regUserByEmailAndCode(userEmail,userVerifyCode);
        if (user!=null){
            if (userService.activeUserByEmail(userEmail)){
                session.setAttribute("user",user);
                return ResultInfo.SUCCESS_INFO;
            }else {
                return ResultInfo.FAIL_INFO;
            }
        }else {
            return ResultInfo.FAIL_INFO;
        }
    }

    /**
     * 方法实现说明 邮箱以验证密码登录
     * @author      lxy
     * @Param:      userEmail，userPwd
     * @return
     * @exception
     * @date        2019/4/19 21:37
     */
    @RequestMapping(value = "/loginByEmailAndPwd")
    @ResponseBody
    public String loginByEmailAndPwd(String userEmail, String userPwd, User user, HttpSession session){
        if (userEmail.equals("") || userPwd.equals("")){
            return ResultInfo.NULL_INFO;
        }
        user= userService.loginUserByEmailAndPwd(userEmail,userPwd);
        System.out.println(user);
        if (user!=null){
            session.setAttribute("user",user);
            return ResultInfo.SUCCESS_INFO;
        }else {
            return ResultInfo.FAIL_INFO;
        }
    }
    /**
     * 方法实现说明
     * @author      lxy
     * @Param:
     * @return
     * @exception
     * @date        2019/4/21 3:32
     */
    @RequestMapping(value = "/getOldEmailPwd")
    @ResponseBody
    public String getOldEmailPwd(String userEmail,User user){
        if(userEmail.equals("")){
            return ResultInfo.NULL_INFO;
        }
        if(!user.getUserEmail().matches(RegexpUtil.RegExp_Mail)) {
            return ResultInfo.FAIL_INFO;
        }
        user=userService.queryUserByEmail(userEmail);
        if (user!=null){
            new Thread(new MailUtil(user.getUserEmail(), user.getUserPwd())).start();
            return ResultInfo.SUCCESS_INFO;
        }else {
            return ResultInfo.EXIST_INFO;
        }

    }




    //-----------------------------------手机方式-----------------------------
    /**
     * 方法实现说明
     * @author      lxy 手机点击发送密码，插入密码和手机号，状态为0
     * @Param:      userVerifyCode，userPhone
     * @return      json String "0","3"
     * @exception
     * @date        2019/4/20 9:28
     */
    @RequestMapping("sendRegPhonePwd")
    @ResponseBody
    public String sendRegPhonePwd(User user) {
        if(user.getUserPhone().equals("") || user==null){
            return ResultInfo.NULL_INFO;
        }
        if(!user.getUserPhone().matches(RegexpUtil.RegExp_PHONE)) {
            return ResultInfo.FAIL_INFO;
        }
        if (userService.queryUserByPhone(user.getUserPhone())!=null){
            return ResultInfo.EXIST_INFO;
        }
       if(userService.sendRegPwd(user)){
           return ResultInfo.SUCCESS_INFO;
       }else {
           return ResultInfo.FAIL_INFO;
       }
    }
    /**
     * 方法实现说明 注册手机号，输入密码，验证密码和手机号，查找，更新，激活状态0--->1
     *
     * @author      lxy
     * @Param:      userVerifyCode，userPhone,activa
     * @return      json String "0","1","3"
     * @exception
     * @date        2019/4/19 17:22
     */
    @RequestMapping("regByPhone")
    @ResponseBody
    public String regByPhone(String userPhone,String userPwd,User user,HttpSession session){
        if (userPwd.equals("") || userPhone.equals("")){
            return ResultInfo.NULL_INFO;
        }
        if (userService.regUserByPhoneAndPwd(userPhone,userPwd)!=null){
            if (userService.activeUserByPhone(userPhone)){
                user=userService.regUserByPhoneAndPwd(userPhone,userPwd);
                session.setAttribute("user",user);
                return ResultInfo.SUCCESS_INFO;
            }else {
                return ResultInfo.FAIL_INFO;
            }
        }else {
            return ResultInfo.FAIL_INFO;
        }
    }
    /**
     * 方法实现说明 点击发送验证码，更换验证码
     * @author      lxy
     * @Param:      userVerifyCode,userPhone
     * @return
     * @exception
     * @date        2019/4/20 16:18
     */
    @RequestMapping("sendLoginPhonePwd")
    @ResponseBody
    public String sendLoginPhonePwd(User user){
        if(user.getUserPhone().equals("") || user==null){
            return ResultInfo.NULL_INFO;
        }

        if(userService.sendLoginMessage(user)){
            return ResultInfo.SUCCESS_INFO;
        }else {
            return ResultInfo.FAIL_INFO;
        }
    }




    /**
     * 方法实现说明 验证更新的验证码和手机号，相同则登录，登陆后，激活为1
     * @author      lxy
     * @Param:      userVerifyCode,userPhone
     * @return      json String
     * @exception
     * @date        2019/4/20 16:15
     */
    @RequestMapping("/loginByPhoneAndCode")
    @ResponseBody
    public String loginByPhoneAndCode(String userPhone,String userVerifyCode,User user,HttpSession session){
        if (userVerifyCode.equals("") || userPhone.equals("")){
            return ResultInfo.NULL_INFO;
        }
        if (userService.loginByPhoneAndCode(userPhone,userVerifyCode)!=null){
            if (userService.activeUserByPhone(userPhone)){
                user=userService.loginByPhoneAndCode(userPhone,userVerifyCode);
                session.setAttribute("user",user);
                return ResultInfo.SUCCESS_INFO;
            }else {
                System.out.println("ssssss");
                return ResultInfo.FAIL_INFO;
            }
        }else {
            System.out.println("sdddddddddddddddddddds");
            return ResultInfo.FAIL_INFO;
        }
    }


    /**
     * 方法实现说明
     * @author      lxy 通过手机找回密码，返回登录页面
     * @Param:    userPhone
     * @return      String json
     * @exception
     * @date        2019/4/21 3:32
     */
    @RequestMapping(value = "/getOldPhonePwd")
    @ResponseBody
    public String getOldPhonePwd(String userPhone,User user){
        if(userPhone.equals("")){
            return ResultInfo.NULL_INFO;
        }
        if(!user.getUserPhone().matches(RegexpUtil.RegExp_PHONE)) {
            return ResultInfo.FAIL_INFO;
        }
        user=userService.queryUserByPhone(userPhone);
        if (user!=null){
            new PhoneUtil().sendCode(user.getUserPhone(),user.getUserPwd());
            return ResultInfo.SUCCESS_INFO;
        }else {
            return ResultInfo.EXIST_INFO;
        }

    }

}
