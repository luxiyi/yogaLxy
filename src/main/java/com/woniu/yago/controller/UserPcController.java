package com.woniu.yago.controller;

import com.woniu.yago.constant.SysConstant;
import com.woniu.yago.pojo.Result;
import com.woniu.yago.pojo.User;
import com.woniu.yago.pojo.Venue;
import com.woniu.yago.service.*;
import com.woniu.yago.utils.CodeUtil;
import com.woniu.yago.utils.NickNameUtil;
import com.woniu.yago.utils.RegexpUtil;
import com.woniu.yago.utils.ResultUtil;
import com.woniu.yago.vo.PhoneToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/userPc")
public class UserPcController {
    @Autowired
    private UserService userService;

    @Autowired
    private VenueService venueService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    //-----------------------------------PC端方式-----------------------------
    /**
     * 方法实现说明  发送手机注册的验证码，已封装
     * 主要判断输入的值是否为空，注册判断手机号是否已经存在
     * @author      lxy
     * @Param:      userPwd，userPhone
     * @return      json String "0","3"
     * @exception
     * @date        2019/4/20 9:28
     */
    @RequestMapping("/sendRegPhoneCode")
    @ResponseBody
    public Result sendRegPhoneCode(User user) {
        if(user.getUserPhone().equals("") || user==null){
            return ResultUtil.errorOperation("手机号不能为空");
        }
        if(!user.getUserPhone().matches(RegexpUtil.RegExp_PHONE)) {
            return ResultUtil.errorOperation("手机格式不匹配");
        }
        if (userService.queryUserByPhone(user.getUserPhone())!=null){
            return ResultUtil.errorOperation("该手机号已经被绑定，请重新输入");
        }
        Integer templateId = 316608;
        if(!userService.sendPhoneMessage(user,templateId)){
            return ResultUtil.connectDatabaseFail();
        }
        return ResultUtil.actionSuccess("请在手机上查收验证码",user);

    }
    /**
     * 方法实现说明 注册手机号
     * 1、输入手机、密码和验证码，
     * 2、同时插入默认的头像和昵称,判断角色，分别插入userid到场馆，管理员没有表，不用插入
     * @author      lxy
     * @Param:      userVerifyCode，userPhone,userPwd，active
     * @return      json String
     * @exception
     * @date        2019/4/19 17:22
     */
    @RequestMapping("/regByPhone")
    @ResponseBody
    public Result regByPhone(String userPhone, String userPwd, String userVerifyCode ,User user, HttpSession session,
                          Venue venue){
        if (userPwd.equals("")){
            return ResultUtil.errorOperation("密码不能为空");
        }
        if (userPhone.equals("")){
            return ResultUtil.errorOperation("手机号不能为空");
        }
//        if (userVerifyCode.equals("")){
//            return ResultUtil.errorOperation("验证码不能为空");
//        }
        if(!userPhone.matches(RegexpUtil.RegExp_PHONE)) {
            return ResultUtil.errorOperation("手机格式不匹配，请重新输入");
        }
        if(!userPwd.matches(RegexpUtil.RegExp_PASS)){
            return ResultUtil.errorOperation("密码格式不匹配，请重新输入");
        }
         User exist=userService.queryUserByPhone(userPhone);
        if (exist!=null){
            return ResultUtil.errorOperation("该手机号已经被绑定，请重新输入");
        }
//        if (!stringRedisTemplate.hasKey(userPhone)){
//            return ResultUtil.errorOperation("验证码已过期，请重新获取验证码");
//        }
//        if(!stringRedisTemplate.opsForValue().get(userPhone).equals(userPwd)){
//            return ResultUtil.errorOperation("验证码错误，请重新输入");
//        }
        user.setSalt(CodeUtil.userNumber());
        SimpleHash userHashPwd = new SimpleHash("MD5",userPwd,user.getSalt(),2);
        user.setUserPwd(userHashPwd.toString());
        user.setRoleId(3);
        user.setUserNickname(NickNameUtil.getRandomNickName());
        user.setActive(1);
        userService.saveUser(user);
        if (user.getActive()==0){
            return ResultUtil.errorOperation("注册失败");
        }
        if (user.getRoleId()==3){
            venue.setUserId(user.getUserId());
            venueService.saveVenue(venue);
        }
        PhoneToken token = new PhoneToken(userPhone);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        user = (User) subject.getPrincipal();
        session.setAttribute(SysConstant.CURRENT_USER,user);
        return ResultUtil.actionSuccess("注册成功",user);
    }



    /**
     * 方法实现说明 手机登录
     * 1、判断是否注册，为空等
     * 2、以密码登录，在数据库中验证密码和手机号，判断是否正确
     *
     *
     * @author      lxy
     * @Param:      userPwd,userPhone
     * @return      json String
     * @exception
     * @date        2019/4/20 16:15
     */
    @RequestMapping("/loginByPhoneAndPwd")
    @ResponseBody
    public Result loginByPhoneAndPwd(String userPhone,String userPwd,User user,HttpSession session){
        user=userService.queryUserByPhone(userPhone);
        if (user==null){
            return ResultUtil.errorOperation("该手机号没有被注册，请先登录再注册");
        }
        if(userPhone.equals("")){
            return ResultUtil.errorOperation("手机号不能为空");
        }
        if(userPwd.equals("")){
            return ResultUtil.errorOperation("密码不能为空");
        }
        if (!userPhone.matches(RegexpUtil.RegExp_PHONE)){
            return ResultUtil.errorOperation("手机格式不匹配，请重新输入");
        }
        if (!userPwd.matches(RegexpUtil.RegExp_PASS)){
            return ResultUtil.errorOperation("密码格式不匹配，请重新输入");
        }
        if (user.getRoleId()!=3 && user.getRoleId()!=4){
            return ResultUtil.errorOperation("该用户因权限无法登录Pc端，请重新输入");
        }
        System.out.println(userPhone);
        Subject subject= SecurityUtils.getSubject();
        if (!subject.isAuthenticated()){
            UsernamePasswordToken token = new UsernamePasswordToken(userPhone,userPwd);
            try {
                subject.login(token);
                //认证成功
                System.out.println("认证成功!");
                session.setAttribute(SysConstant.CURRENT_USER, user);
                return ResultUtil.actionSuccess("登录成功",user);
            } catch (UnknownAccountException uae) {
                return ResultUtil.errorOperation("手机号不正确,请重新输入手机号");
            } catch (IncorrectCredentialsException ice) {
                return ResultUtil.errorOperation("密码不正确,请重新输入密码");
            } catch (AuthenticationException ae) {
                return ResultUtil.errorOperation("登录失败");
            }
        }
        return ResultUtil.actionSuccess("登录成功",user);
    }

    /**
     * 方法实现说明 注销
     * @author      lxy
     * @Param:
     * @return
     * @exception
     * @date        2019/4/30 10:46
     */
    @RequestMapping("/logout")
    public String logout() {
        System.out.println("注销");
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:../loginPc.html";
    }


}