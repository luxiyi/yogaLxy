package com.woniu.yago.controller;

import com.woniu.yago.com.woniu.yago.vo.PhoneToken;
import com.woniu.yago.constant.SysConstant;
import com.woniu.yago.pojo.User;
import com.woniu.yago.service.UserService;
import com.woniu.yago.utils.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
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
@Transactional
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
    @RequestMapping(value = "/sendRegEmailCode")
    @ResponseBody
    public String sendRegEmailCode(String userEmail,User user){
        if(userEmail.equals("")){
            return ResultUtil.errorOperation("不能为空").getMessage();
        }
        if(!user.getUserEmail().matches(RegexpUtil.RegExp_Mail)) {
            return ResultUtil.errorOperation("邮箱格式不匹配").getMessage();
        }
        if (userService.queryUserByEmail(userEmail)!=null){
            return ResultUtil.errorOperation("已存在").getMessage();
        }
        user.setUserEmail(userEmail);
        if (userService.sendRegEmailCode(user)){
            return ResultUtil.actionSuccess("前往邮箱获取验证码",user).getMessage();
        }else {
            return ResultUtil.connectDatabaseFail().getMessage();
        }
    }
    /**
     * 方法实现说明 注册邮箱，从邮箱中获取验证码，输入密码，验证密码和邮箱、验证码
     *  密码加密，加盐，盐值采用6位随机数，盐值和密码注册时，插入，注册邮箱直接默认是学生
     *  注册后直接通过认证，登录主页
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
            return ResultUtil.errorOperation("不能为空").getMessage();
        }
        if(!userEmail.matches(RegexpUtil.RegExp_Mail)) {
            return ResultUtil.errorOperation("邮箱格式不匹配").getMessage();
        }
        if (!userPwd.matches(RegexpUtil.RegExp_PASS)){
            return ResultUtil.errorOperation("密码格式不匹配").getMessage();
        }
        user=userService.queryUserByEmailAndCode(userEmail,userVerifyCode);
        if (user!=null){
            user.setRoleId(1);
            user.setSalt(CodeUtil.userNumber());
            SimpleHash userHashPwd = new SimpleHash("MD5",userPwd,user.getSalt(),2);
            user.setUserPwd(userHashPwd.toString());
                if (userService.activeUserByEmail(userEmail)){
                    Subject subject=SecurityUtils.getSubject();
                    UsernamePasswordToken token = new UsernamePasswordToken(userEmail,userPwd);
                    subject.login(token);
                    session.setAttribute(SysConstant.CURRENT_USER,user);
                    return ResultUtil.actionSuccess("注册成功",user).getMessage();
                }else {
                    return ResultUtil.errorOperation("注册失败").getMessage();
                }
        }else {
            return ResultUtil.errorOperation("已存在").getMessage();
        }
    }

    /**
     * 方法实现说明 邮箱以验证密码登录,shiro认证
     * @author      lxy
     * @Param:      userEmail，userPwd
     * @return
     * @exception
     * @date        2019/4/19 21:37
     */
    @RequestMapping(value = "/loginByEmailAndPwd")
    @ResponseBody
    public String loginByEmailAndPwd(String userEmail, String userPwd, User user, HttpSession session,String info){
        Subject subject= SecurityUtils.getSubject();
        if (!subject.isAuthenticated()){
            UsernamePasswordToken token = new UsernamePasswordToken(userEmail,userPwd);
            try {
                subject.login(token);
                //认证成功
                System.out.println("认证成功!");
                session.setAttribute(SysConstant.CURRENT_USER, user);
                return ResultUtil.actionSuccess("登录成功",user).getMessage();
            } catch (UnknownAccountException uae) {
                return ResultUtil.errorOperation("账号不正确,请重新输入账号").getMessage();
            } catch (IncorrectCredentialsException ice) {
                return ResultUtil.errorOperation("密码不正确,请重新输入密码").getMessage();
            } catch (AuthenticationException ae) {
                return ResultUtil.errorOperation("登录失败").getMessage();
            }
        }
            return ResultUtil.actionSuccess("登录成功",user).getMessage();
    }
    /**
     * 方法实现说明  邮箱找回密码，通过用户名发送验证码邮件，插入更换验证码
     * @author      lxy
     * @Param:      userEmail，userVerifyCode
     * @return      String json
     * @exception
     * @date        2019/4/21 3:32
     */
    @RequestMapping(value = "/getCodeByEmail")
    @ResponseBody
    public String getCodeByEmail(String userEmail,User user){
        if(userEmail.equals("")){
            return ResultUtil.errorOperation("不能为空").getMessage();
        }
        if(!userEmail.matches(RegexpUtil.RegExp_Mail)) {
            return ResultUtil.errorOperation("邮箱格式不匹配").getMessage();
        }
        user=userService.queryUserByEmail(userEmail);
        String userVerifyCode = CodeUtil.userNumber();
        String content = "<html><head></head><body><h1>这是一封绝密邮件,不要随便将内容透露给别人。" +
                "</h1><br><h3>您本次找回密码所需的的验证码为：" + userVerifyCode + "。</h3></body></html>";
        if (user!=null){
                user.setUserVerifyCode(userVerifyCode);
                new Thread(new MailUtil(userEmail,userVerifyCode,content)).start();
            return ResultUtil.actionSuccess("前往邮箱获取验证码",user).getMessage();
        }else {
            return ResultUtil.errorOperation("邮箱不存在").getMessage();
        }
    }
    /**
     * 方法实现说明 邮箱找回密码，验证验证码与邮箱是否正确，跳转到重置密码页面
     * @author      lxy
     * @Param:
     * @return
     * @exception
     * @date        2019/4/23 22:38
     */
        @RequestMapping("/getPwdByEmail")
        @ResponseBody
        public String getPwdByEmail(String userEmail,String userVerifyCode,User user,HttpSession session){
            if (userEmail.equals("") || userVerifyCode.equals("")){
                return ResultUtil.errorOperation("不能为空").getMessage();
            }
            if(!userEmail.matches(RegexpUtil.RegExp_Mail)) {
                return ResultUtil.errorOperation("邮箱格式不匹配").getMessage();
            }
            user=userService.queryUserByEmailAndCode(userEmail,userVerifyCode);
            if (user!=null){
                session.setAttribute(SysConstant.CURRENT_USER,user);
                return ResultUtil.actionSuccess("请重置密码",user).getMessage();
            }else {
                return ResultUtil.errorOperation("验证码错误").getMessage();
            }
        }
        /**
         * 方法实现说明 重置密码
         * @author      lxy
         * @Param:
         * @return
         * @exception
         * @date        2019/4/24 0:03
         */
        @RequestMapping("/ updateUserPwdByEmail")
        @ResponseBody
        public String updateUserPwdByEmail(String userPwd, String confirmPwd,User user,HttpSession session){
            if (userPwd.equals("") || confirmPwd.equals("")){
                return ResultUtil.errorOperation("不能为空").getMessage();
            }
            if (!userPwd.matches(RegexpUtil.RegExp_PASS) || !confirmPwd.matches(RegexpUtil.RegExp_PASS)){
                return ResultUtil.errorOperation("密码格式不匹配").getMessage();
            }
            user= (User) session.getAttribute(SysConstant.CURRENT_USER);
            if (userPwd.equals(confirmPwd)){
                user=userService.queryUserByEmail(user.getUserEmail());
                user.setSalt(CodeUtil.userNumber());
                SimpleHash userHashPwd = new SimpleHash("MD5",userPwd,user.getSalt(),2);
                user.setUserPwd(userHashPwd.toString());
                return ResultUtil.actionSuccess("重置密码成功",user).getMessage();
            }else {
                return ResultUtil.errorOperation("确认密码错误，请重新输入").getMessage();
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
            return ResultUtil.errorOperation("不能为空").getMessage();
        }
        if(!user.getUserPhone().matches(RegexpUtil.RegExp_PHONE)) {
            return ResultUtil.errorOperation("手机格式不匹配").getMessage();
        }
        if (userService.queryUserByPhone(user.getUserPhone())!=null){
            return ResultUtil.errorOperation("手机号已存在").getMessage();
        }
       if(userService.sendRegPhonePwd(user)){
            return ResultUtil.actionSuccess("请在手机上查收密码",user).getMessage();
       }else {
           return ResultUtil.connectDatabaseFail().getMessage();
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
    @RequestMapping("/regByPhone")
    @ResponseBody
    public String regByPhone(String userPhone,String userPwd,User user,Integer roleId,HttpSession session){
        if (userPwd.equals("") || userPhone.equals("") || roleId == 0){
            return ResultUtil.errorOperation("不能为空").getMessage();
        }
        if(!userPhone.matches(RegexpUtil.RegExp_PHONE)) {
            return ResultUtil.errorOperation("手机格式不匹配").getMessage();
        }
        user=userService.queryUserByPhoneAndPwd(userPhone,userPwd);
        if (user!=null){
            if (userService.activeUserByPhone(userPhone)){
                user.setSalt(CodeUtil.userNumber());
                SimpleHash userHashPwd = new SimpleHash("MD5",userPwd,user.getSalt(),2);
                user.setUserPwd(userHashPwd.toString());
                System.out.println(roleId);
                user.setRoleId(roleId);
                Subject subject=SecurityUtils.getSubject();
                UsernamePasswordToken token = new UsernamePasswordToken(userPhone,userPwd);
                subject.login(token);
                session.setAttribute(SysConstant.CURRENT_USER,user);
                return ResultUtil.actionSuccess("注册成功",user).getMessage();
            }else {
                return ResultUtil.errorOperation("注册失败").getMessage();
            }
        }else {
            System.out.println(user);
            return ResultUtil.errorOperation("已存在").getMessage();
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
            return ResultUtil.errorOperation("不能为空").getMessage();
        }
        if(!user.getUserPhone().matches(RegexpUtil.RegExp_PHONE)) {
            return ResultUtil.errorOperation("手机格式不匹配").getMessage();
        }
        if(userService.sendLoginMessage(user)){
            return ResultUtil.actionSuccess("请在手机上查收验证码",user).getMessage();
        }else {
            return ResultUtil.connectDatabaseFail().getMessage();
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
        if (!userPhone.matches(RegexpUtil.RegExp_PHONE)){
            return ResultUtil.errorOperation("手机格式不匹配").getMessage();
        }
        user=userService.queryUserByPhoneAndCode(userPhone,userVerifyCode);
        if (user!=null){
           if (userService.activeUserByPhone(userPhone)){
            PhoneToken token = new PhoneToken(userPhone);
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            user = (User) subject.getPrincipal();
                session.setAttribute(SysConstant.CURRENT_USER,user);
                return ResultUtil.actionSuccess("登录成功",user).getMessage();
            }else {
                return ResultUtil.errorOperation("登录失败").getMessage();
            }
        }else {
            return ResultUtil.errorOperation("登录失败").getMessage();
        }

    }


    /**
     * 方法实现说明
     * @author      lxy  手机找回密码，通过手机发送验证码，插入更换验证码
     * @Param:    userPhone
     * @return      String json
     * @exception
     * @date        2019/4/21 3:32
     */
    @RequestMapping(value = "/getCodeByPhone")
    @ResponseBody
    public String getCodeByPhone(String userPhone,User user){
        if(userPhone.equals("")){
            return ResultUtil.errorOperation("不能为空").getMessage();
        }
        if(!user.getUserPhone().matches(RegexpUtil.RegExp_PHONE)) {
            return ResultUtil.errorOperation("手机格式不匹配").getMessage();
        }
        user=userService.queryUserByPhone(userPhone);
        String userVerifyCode = CodeUtil.userNumber();
        if (user!=null){
            user.setUserVerifyCode(userVerifyCode);
            new PhoneUtil().sendCode(userPhone,userVerifyCode);
            return ResultUtil.actionSuccess("请在手机上查收验证码",user).getMessage();
        }else {
            return ResultUtil.errorOperation("手机号不存在").getMessage();
        }
    }

    /**
     * 方法实现说明 手机找回密码，验证验证码与手机是否正确，跳转到重置密码页面
     * @author      lxy
     * @Param:
     * @return
     * @exception
     * @date        2019/4/23 22:38
     */
    @RequestMapping("/getPwdByPhone")
    @ResponseBody
    public String getPwdByPhone(String userPhone,String userVerifyCode,User user,HttpSession session){
        if (userPhone.equals("") || userVerifyCode.equals("")){
            return ResultUtil.errorOperation("不能为空").getMessage();
        }
        if(!userPhone.matches(RegexpUtil.RegExp_PHONE)) {
            return ResultUtil.errorOperation("手机格式不匹配").getMessage();
        }
        user=userService.queryUserByPhoneAndCode(userPhone,userVerifyCode);
        if (user!=null){
            session.setAttribute(SysConstant.CURRENT_USER,user);
            return ResultUtil.actionSuccess("请重置密码",user).getMessage();
        }else {
            return ResultUtil.errorOperation("验证码错误").getMessage();
        }
    }
    /**
     * 方法实现说明 重置密码
     * @author      lxy
     * @Param:
     * @return
     * @exception
     * @date        2019/4/24 0:03
     */
    @RequestMapping("/updateUserPwdByPhone")
    @ResponseBody
    public String updateUserPwdByPhone(String userPwd, String confirmPwd,User user,HttpSession session){
        if (userPwd.equals("") || confirmPwd.equals("")){
            return ResultUtil.errorOperation("不能为空").getMessage();
        }
        if (!userPwd.matches(RegexpUtil.RegExp_PASS) || !confirmPwd.matches(RegexpUtil.RegExp_PASS)){
            return ResultUtil.errorOperation("密码格式不匹配").getMessage();
        }
        user= (User) session.getAttribute(SysConstant.CURRENT_USER);
        if (userPwd.equals(confirmPwd)){
            user=userService.queryUserByPhone(user.getUserPhone());
            user.setSalt(CodeUtil.userNumber());
            SimpleHash userHashPwd = new SimpleHash("MD5",userPwd,user.getSalt(),2);
            user.setUserPwd(userHashPwd.toString());
            return ResultUtil.actionSuccess("重置密码成功",user).getMessage();
        }else {
            return ResultUtil.errorOperation("确认密码错误，请重新输入").getMessage();
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session, Model model) {
        System.out.println("注销");
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        model.addAttribute("msg","安全退出！");
        return "/login.html";
    }
}
