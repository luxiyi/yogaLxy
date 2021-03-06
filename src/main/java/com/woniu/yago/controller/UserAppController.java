package com.woniu.yago.controller;

import com.woniu.yago.vo.PhoneToken;
import com.woniu.yago.constant.SysConstant;
import com.woniu.yago.pojo.*;
import com.woniu.yago.service.*;
import com.woniu.yago.utils.*;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
@RequestMapping("/userApp")
public class UserAppController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CoachService coachService;
    @Autowired
    private VenueService venueService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //-----------------------------------邮箱方式-----------------------------

    /**
     * 方法实现说明 发送邮箱验证码，已封装
     * @author      lxy
     * @Param:      email，password，userName
     * @return      json String "success" "fail"
     * @exception
     * @date        2019/4/17 16:26
     */
    @RequestMapping(value = "/sendRegEmailCode")
    @ResponseBody
    public Result sendRegEmailCode(String userEmail, User user){
        if(userEmail.equals("")){
            return ResultUtil.errorOperation("邮箱号不能为空");
        }
        if(!user.getUserEmail().matches(RegexpUtil.RegExp_Mail)) {
            return ResultUtil.errorOperation("邮箱格式不匹配");
        }
        if (userService.queryUserByEmail(userEmail)!=null){
            return ResultUtil.errorOperation("该邮箱号已被注册，请重新输入");
        }
        user.setUserEmail(userEmail);
        if (!userService.sendRegEmailCode(user)){
            return ResultUtil.connectDatabaseFail();
        }
        return ResultUtil.actionSuccess("前往邮箱获取验证码",user);

    }
    /**
     * 方法实现说明 注册邮箱
     * 1、在过期时间内从邮箱中获取验证码，输入密码，验证密码和邮箱
     * 2、输入后点击注册，注册时，先判断数据库中是否存在
     * 3、如果不存在，则在redis中查找是否有邮箱的键，如果有，说明没有过期
     * 则成功，成功后再将邮箱和密码插入进数据库，最后将激活码激活0-->1
     * 3、如果在redis中没有查到邮箱账号，那么失败，返回字符串
     *  密码加密，加盐，盐值采用6位随机数，盐值和密码注册时，插入，注册邮箱直接默认是学生
     *  同时插入默认的昵称和头像
     *  注册后直接通过认证，登录主页
     *  注意 插入user表后，还要分别插入对应的教练表和学生表 验证码使用redis插入缓存
     * @author      lxy
     * @Param:      userVerifyCode，userPhone,active
     * @return      json String "0","1","3"
     * @exception
     * @date        2019/4/19 17:22
     */
    @RequestMapping("regByEmail")
    @ResponseBody
    public Result regByEmail(String userEmail, String userPwd, String userVerifyCode, User user, HttpSession session,
                             String roleName, Student student){
        if (userPwd.equals("") || userEmail.equals("") || userVerifyCode.equals("")){
            return ResultUtil.errorOperation("邮箱号、密码、验证码不能为空");
        }
        if(!userEmail.matches(RegexpUtil.RegExp_Mail)) {
            return ResultUtil.errorOperation("邮箱格式不匹配");
        }
        if (!userPwd.matches(RegexpUtil.RegExp_PASS)){
            return ResultUtil.errorOperation("密码格式不匹配");
        }
        User exit=userService.queryUserByEmail(userEmail);
        if (exit!=null){
            return ResultUtil.errorOperation("该邮箱号已被注册，请重新输入");
        }
        Role role=roleService.findByRoleName(roleName);
        if (role == null){
            return ResultUtil.errorOperation("职业选择错误，请重新选择职业");
        }
//        if(!stringRedisTemplate.hasKey(userEmail)){
//            return ResultUtil.errorOperation("验证码过期，请重新发送验证码");
//        }
//        if (!stringRedisTemplate.opsForValue().get(userEmail).equals(userVerifyCode)){
//            return ResultUtil.errorOperation("验证码错误，请重新输入");
//        }
        user.setUserVerifyCode("");
        user.setRoleId(role.getRoleId());
        user.setSalt(CodeUtil.userNumber());
        SimpleHash userHashPwd = new SimpleHash("MD5",userPwd,user.getSalt(),2);
        user.setUserPwd(userHashPwd.toString());
        user.setUserNickname(NickNameUtil.getRandomNickName());
        user.setActive(1);
        userService.saveUser(user);
        if (user.getActive()==0){
            return ResultUtil.errorOperation("注册失败");
        }
        Subject subject=SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userEmail,userPwd);
        subject.login(token);
        student.setUserId(user.getUserId());
        studentService.saveStudent(student);
        session.setAttribute(SysConstant.CURRENT_USER,user);
        return ResultUtil.actionSuccess("注册成功",user);
    }

    /**
     * 方法实现说明 邮箱以密码登录,shiro认证
     * @author      lxy
     * @Param:      userEmail，userPwd
     * @return
     * @exception
     * @date        2019/4/19 21:37
     */
    @RequestMapping(value = "/loginByEmailAndPwd")
    @ResponseBody
    public Result loginByEmailAndPwd(String userEmail, String userPwd, User user, HttpSession session){
        user=userService.queryUserByEmail(userEmail);
        if (user==null){
            return ResultUtil.errorOperation("该邮箱号没有被注册，请先注册再登录");
        }
        if (user.getRoleId()!=1 && user.getRoleId()!=2){
                return ResultUtil.errorOperation("该用户因权限无法登录App端，请重新输入");
        }
        Subject subject= SecurityUtils.getSubject();
        if (!subject.isAuthenticated()){
            UsernamePasswordToken token = new UsernamePasswordToken(userEmail,userPwd);

            try {
                subject.login(token);
                //认证成功
                System.out.println("认证成功!");

                session.setAttribute(SysConstant.CURRENT_USER, user);
                Result result=ResultUtil.actionSuccess("登录成功",user);
                return ResultUtil.actionSuccess("登录成功",user);
            } catch (UnknownAccountException uae) {
                return ResultUtil.errorOperation("账号不正确,请重新输入账号");
            } catch (IncorrectCredentialsException ice) {
                return ResultUtil.errorOperation("密码不正确,请重新输入密码");
            } catch (AuthenticationException ae) {
                return ResultUtil.errorOperation("登录失败");
            }
        }
            return ResultUtil.actionSuccess("登录成功",user);
    }
    /**
     * 方法实现说明  发送验证码（邮箱找回密码）
     * 1、已经有用户名，因此不用考虑插入，而是更新
     * 2、先查找邮箱是否存在
     * 3、存在发送验证码时先以键值对插入redis
     * 4、判断是否插入，已插入则发送，同时设置过期时间
     * @author      lxy
     * @Param:      userEmail，userVerifyCode
     * @return      String json
     * @exception
     * @date        2019/4/21 3:32
     */
    @RequestMapping(value = "/getCodeByEmail")
    @ResponseBody
    public Result getCodeByEmail(String userEmail,User user){
        if(userEmail.equals("")){
            return ResultUtil.errorOperation("邮箱号不能为空");
        }
        if(!userEmail.matches(RegexpUtil.RegExp_Mail)) {
            return ResultUtil.errorOperation("邮箱格式不匹配");
        }
        user=userService.queryUserByEmail(userEmail);
        if (user==null){
            return ResultUtil.errorOperation("该邮箱号不存在，请重新输入");
        }
        String userVerifyCode = CodeUtil.userNumber();
        String content = "<html><head></head><body><h1>这是一封绝密邮件,不要随便将内容透露给别人。" +
                "</h1><br><h3>您本次找回密码所需的验证码为：" + userVerifyCode + "。</h3></body></html>";
        stringRedisTemplate.opsForValue().set(userEmail,userVerifyCode);
        if (!stringRedisTemplate.hasKey(userEmail)){
            return ResultUtil.connectDatabaseFail();
        }
         new Thread(new MailUtil(userEmail,userVerifyCode,content)).start();
        stringRedisTemplate.expire(userEmail,30, TimeUnit.SECONDS);
         return ResultUtil.actionSuccess("前往邮箱获取验证码",user);

    }
    /**
     * 方法实现说明 登录重置密码界面（邮箱找回密码）
     * 1、在redis中先判断验证码是否过期
     * 2、没有过期则，在redis中判断验证码与邮箱是否正确
     * 2、正确，则跳转到重置密码页面
     * @author      lxy
     * @Param:
     * @return
     * @exception
     * @date        2019/4/23 22:38
     */
        @RequestMapping("/getPwdByEmail")
        @ResponseBody
        public Result getPwdByEmail(String userEmail,String userVerifyCode,User user,HttpSession session){
            if (userEmail.equals("") || userVerifyCode.equals("")){
                return ResultUtil.errorOperation("邮箱号或验证码不能为空");
            }
            if(!userEmail.matches(RegexpUtil.RegExp_Mail)) {
                return ResultUtil.errorOperation("邮箱格式不匹配");
            }
            if (!stringRedisTemplate.hasKey(userEmail)){
                return ResultUtil.errorOperation("验证码已过期，请重新点击获取");
            }
            if (!stringRedisTemplate.opsForValue().get(userEmail).equals(userVerifyCode)){
                return ResultUtil.errorOperation("验证码错误，请重新输入");
            }
            session.setAttribute(SysConstant.CURRENT_USER,user);
            return ResultUtil.actionSuccess("验证成功，跳转下一步",user);

        }
        /**
         * 方法实现说明 重置密码
         * @author      lxy
         * @Param:
         * @return
         * @exception
         * @date        2019/4/24 0:03
         */
        @RequestMapping("/updateUserPwdByEmail")
        @ResponseBody
        public Result updateUserPwdByEmail(String userPwd, String confirmPwd,User user,HttpSession session){
            if (userPwd.equals("") || confirmPwd.equals("")){
                return ResultUtil.errorOperation("密码或确认密码不能为空");
            }
            if (!userPwd.matches(RegexpUtil.RegExp_PASS) || !confirmPwd.matches(RegexpUtil.RegExp_PASS)){
                return ResultUtil.errorOperation("密码格式不匹配");
            }
            user= (User) session.getAttribute(SysConstant.CURRENT_USER);
            if (!userPwd.equals(confirmPwd)){
                return ResultUtil.errorOperation("确认密码错误，请重新输入");
            }
                user=userService.queryUserByEmail(user.getUserEmail());
                user.setSalt(CodeUtil.userNumber());
                SimpleHash userHashPwd = new SimpleHash("MD5",userPwd,user.getSalt(),2);
                user.setUserPwd(userHashPwd.toString());
                return ResultUtil.actionSuccess("重置密码成功",user);

        }


    //-----------------------------------手机方式-----------------------------
    /**
     * 方法实现说明  发送手机注册的密码，已封装
     * 主要判断输入的值是否为空，注册判断手机号是否已经存在
     * @author      lxy
     * @Param:      userPwd，userPhone
     * @return      json String "0","3"
     * @exception
     * @date        2019/4/20 9:28
     */
    @RequestMapping("/sendRegPhonePwd")
    @ResponseBody
    public Result sendRegPhonePwd(User user) {
        if(user.getUserPhone().equals("") || user==null){
            return ResultUtil.errorOperation("手机号不能为空");
        }
        if(!user.getUserPhone().matches(RegexpUtil.RegExp_PHONE)) {
            return ResultUtil.errorOperation("手机格式不匹配");
        }
        if (userService.queryUserByPhone(user.getUserPhone())!=null){
            return ResultUtil.errorOperation("该手机号已经被绑定，请重新输入");
        }
        Integer templateId = 317053;
        if(!userService.sendPhoneMessage(user,templateId)){
            return ResultUtil.connectDatabaseFail();
        }
        return ResultUtil.actionSuccess("请在手机上查收密码",user);

    }
    /**
     * 方法实现说明 注册手机号，输入密码，验证密码和手机号，查找，更新，激活状态0--->1
     *              同时插入默认的头像和昵称,判断角色，分别插入userid到学员，教练，场馆
     * @author      lxy
     * @Param:      userVerifyCode，userPhone,activa
     * @return      json String "0","1","3"
     * @exception
     * @date        2019/4/19 17:22
     */
    @RequestMapping("/regByPhone")
    @ResponseBody
    public Result regByPhone(String userPhone, String userPwd, User user, String roleName, HttpSession session,
                             Student student, Coach coach){
        if (userPwd.equals("")){
            return ResultUtil.errorOperation("密码不能为空");
        }
        if (userPhone.equals("")){
            return ResultUtil.errorOperation("手机号不能为空");
        }
        if (roleName.equals("")){
            return ResultUtil.errorOperation("职业不能为空");
        }
        if(!userPhone.matches(RegexpUtil.RegExp_PHONE)) {
            return ResultUtil.errorOperation("手机格式不匹配");
        }
         User exist=userService.queryUserByPhone(userPhone);
        if (exist!=null){
            return ResultUtil.errorOperation("该手机号已经被绑定，请重新输入");
        }
//        if (!stringRedisTemplate.hasKey(userPhone)){
//            return ResultUtil.errorOperation("密码已过期，请重新获取密码");
//        }
//        if(!stringRedisTemplate.opsForValue().get(userPhone).equals(userPwd)){
//            return ResultUtil.errorOperation("密码错误，请重新输入");
//        }
        user.setSalt(CodeUtil.userNumber());
        SimpleHash userHashPwd = new SimpleHash("MD5",userPwd,user.getSalt(),2);
        user.setUserPwd(userHashPwd.toString());
        Role role=roleService.findByRoleName(roleName);
        if (role == null){
            return ResultUtil.errorOperation("职业选择错误，请重新选择职业");
        }

        user.setRoleId(role.getRoleId());
        user.setUserNickname(NickNameUtil.getRandomNickName());
        user.setActive(1);
        userService.saveUser(user);
        if (user.getActive()==0){
            return ResultUtil.errorOperation("注册失败");
        }
        if (user.getRoleId()==1){
            student.setUserId(user.getUserId());
            studentService.saveStudent(student);
        }
        if (user.getRoleId()==2){
            System.out.println("Ssfsdsdsds"+coach);
            coach.setUserId(user.getUserId());
            coachService.saveCoach(coach);
        }


        Subject subject=SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userPhone,userPwd);
        subject.login(token);
        session.setAttribute(SysConstant.CURRENT_USER,user);
        return ResultUtil.actionSuccess("注册成功",user);
    }
    /**
     * 方法实现说明  发送登录手机的验证码，已封装
     * 主要判断输入的值是否为空，登录判断手机号是否已经存在
     * @author      lxy
     * @Param:      userVerifyCode,userPhone
     * @return
     * @exception
     * @date        2019/4/20 16:18
     */
    @RequestMapping("/sendLoginPhonePwd")
    @ResponseBody
    public Result sendLoginPhonePwd(User user){
        if(user.getUserPhone().equals("") || user==null){
            return ResultUtil.errorOperation("手机号不能为空");
        }
        if(!user.getUserPhone().matches(RegexpUtil.RegExp_PHONE)) {
            return ResultUtil.errorOperation("手机格式不匹配");
        }
       if (userService.queryUserByPhone(user.getUserPhone())==null){
           System.out.println();
           return ResultUtil.errorOperation("该手机号没有注册，请先注册再登录");
       }
        Integer templateId = 316608;
        if(!userService.sendPhoneMessage(user,templateId)){
           return ResultUtil.connectDatabaseFail();
        }
        return ResultUtil.actionSuccess("请在手机上查收验证码",user);

    }




    /**
     * 方法实现说明 手机登录
     * 1、判断是否注册，为空等
     * 2、以验证码登录，在redis中验证验证码和手机号，判断验证码是否过期，是否正确
     *
     *
     * @author      lxy
     * @Param:      userVerifyCode,userPhone
     * @return      json String
     * @exception
     * @date        2019/4/20 16:15
     */
    @RequestMapping("/loginByPhoneAndCode")
    @ResponseBody
    public Result loginByPhoneAndCode(String userPhone,String userVerifyCode,User user,HttpSession session){
        if(userPhone.equals("")){
            return ResultUtil.errorOperation("手机号不能为空");
        }
//        if(userVerifyCode.equals("")){
//            return ResultUtil.errorOperation("验证码不能为空");
//        }
        if (!userPhone.matches(RegexpUtil.RegExp_PHONE)){
            return ResultUtil.errorOperation("手机格式不匹配");
        }
        System.out.println(userPhone);
        user=userService.queryUserByPhone(userPhone);
        if (user==null){
            return ResultUtil.errorOperation("该手机号没有注册，请先注册再登录");
        }
        if (user.getRoleId()!=1 && user.getRoleId()!=2){
            System.out.println(user.getRoleId());
            return ResultUtil.errorOperation("该用户因权限无法登录App端，请重新输入");
        }
//        if (!stringRedisTemplate.hasKey(userPhone)){
//            return ResultUtil.errorOperation("验证码已过期，请重新点击获取");
//        }
//        if (!stringRedisTemplate.opsForValue().get(userPhone).equals(userVerifyCode)){
//            return ResultUtil.errorOperation("验证码错误，请重新输入");
//        }
        PhoneToken token = new PhoneToken(userPhone);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        user = (User) subject.getPrincipal();
        session.setAttribute(SysConstant.CURRENT_USER,user);
        return ResultUtil.actionSuccess("登录成功",user);

    }


    /**
     * 方法实现说明   手机发送找回密码的验证码，已封装
     * @author      lxy
     * @Param:    userPhone
     * @return      String json
     * @exception
     * @date        2019/4/21 3:32
     */
    @RequestMapping(value = "/getCodeByPhone")
    @ResponseBody
    public Result getCodeByPhone(String userPhone,User user){
        if(userPhone.equals("")){
            return ResultUtil.errorOperation("手机号不能为空");
        }
        if(!user.getUserPhone().matches(RegexpUtil.RegExp_PHONE)) {
            return ResultUtil.errorOperation("手机格式不匹配");
        }
        user=userService.queryUserByPhone(userPhone);
        if (user!=null){
            return ResultUtil.errorOperation("手机号没有注册，请重新输入");
        }
        int templateId= 316608;
        if(!userService.sendPhoneMessage(user,templateId)){
            return ResultUtil.connectDatabaseFail();
        }
        return ResultUtil.actionSuccess("请在手机上查收验证码",user);

    }

    /**
     * 方法实现说明 登录重置密码界面
     * 1、在redis中验证验证码与手机是否过期，是否正确
     * 2、跳转到重置密码页面
     * @author      lxy
     * @Param:
     * @return
     * @exception
     * @date        2019/4/23 22:38
     */
    @RequestMapping("/getPwdByPhone")
    @ResponseBody
    public Result getPwdByPhone(String userPhone,String userVerifyCode,User user,HttpSession session){
        if (userPhone.equals("") || userVerifyCode.equals("")){
            return ResultUtil.errorOperation("手机号或者验证码不能为空");
        }
        if(!userPhone.matches(RegexpUtil.RegExp_PHONE)) {
            return ResultUtil.errorOperation("手机格式不匹配");
        }
        user=userService.queryUserByPhone(userPhone);
        if (user==null){
            return ResultUtil.errorOperation("手机号没有注册，请重新输入");
        }
        if (!stringRedisTemplate.hasKey(userPhone)){
            return ResultUtil.errorOperation("验证码已过期，请重新点击获取");
        }
        if (!stringRedisTemplate.opsForValue().get(userPhone).equals(userVerifyCode)){
            return ResultUtil.errorOperation("验证码错误，请重新输入");
        }
        session.setAttribute(SysConstant.CURRENT_USER,user);
        return ResultUtil.actionSuccess("验证成功，跳转下一步",user);

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
    public Result updateUserPwdByPhone(String userPwd, String confirmPwd,User user,HttpSession session){
        if (userPwd.equals("") || confirmPwd.equals("")){
            return ResultUtil.errorOperation("密码或确认密码不能为空");
        }
        if (!userPwd.matches(RegexpUtil.RegExp_PASS) || !confirmPwd.matches(RegexpUtil.RegExp_PASS)){
            return ResultUtil.errorOperation("密码格式不匹配");
        }
        user= (User) session.getAttribute(SysConstant.CURRENT_USER);
        if (userPwd.equals(confirmPwd)){
            user=userService.queryUserByPhone(user.getUserPhone());
            user.setSalt(CodeUtil.userNumber());
            SimpleHash userHashPwd = new SimpleHash("MD5",userPwd,user.getSalt(),2);
            user.setUserPwd(userHashPwd.toString());
            return ResultUtil.actionSuccess("重置密码成功",user);
        }else {
            return ResultUtil.errorOperation("确认密码错误，请重新输入");
        }
    }

    @RequestMapping("/logout")
    public String logout() {
        System.out.println("注销");
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:../loginApp.html";
    }

//-----------------------学生信息------------------------



    /**
     * 方法实现说明   手机发送手机绑定的验证码
     * 通过手机发送验证码，插入更换验证码，手机已存在则不能绑定
     * @author      lxy
     * @Param:    userPhone
     * @return      String json
     * @exception
     * @date        2019/4/21 3:32
     */
    @RequestMapping(value = "/sendBindPhoneCode")
    @ResponseBody
    public Result sendBindPhoneCode(String userPhone,User user,HttpSession session){
        if(userPhone.equals("")){
            return ResultUtil.errorOperation("手机号不能为空");
        }
        if(!user.getUserPhone().matches(RegexpUtil.RegExp_PHONE)) {
            return ResultUtil.errorOperation("手机格式不匹配");
        }
        User exist=userService.queryUserByPhone(userPhone);
        if (exist!=null){
            return ResultUtil.errorOperation("手机号已被绑定，请重新输入手机号");
        }
        int templateId = 316608;
        if(!userService.sendPhoneMessage(user,templateId)){
            return ResultUtil.connectDatabaseFail();
        }
        User userSession=(User)session.getAttribute(SysConstant.CURRENT_USER);
        user=userService.queryUserByEmail(userSession.getUserEmail());
        return ResultUtil.actionSuccess("请在手机上查收验证码",user);

    }


    /**
     * 方法实现说明 绑定手机
     * @author      lxy
     * @Param:
     * @return
     * @exception
     * @date        2019/4/24 18:53
     */
    @RequestMapping("/bindPhone")
    @ResponseBody
    public Result bindPhone(String userPhone,String userVerifyCode,User user,HttpSession session){
        if (userPhone.equals("") || userVerifyCode.equals("")){
            return ResultUtil.errorOperation("手机号或者验证码不能为空");
        }
        if(!userPhone.matches(RegexpUtil.RegExp_PHONE)) {
            return ResultUtil.errorOperation("手机格式不匹配");
        }
        User exist=userService.queryUserByPhone(userPhone);
        if (exist!=null){
            return ResultUtil.errorOperation("手机号已被绑定，请重新输入手机号");
        }
        if (!stringRedisTemplate.hasKey(userPhone)){
            return ResultUtil.errorOperation("验证码已经过期，请重新点击获取");
        }
        if (!stringRedisTemplate.opsForValue().get(userPhone).equals(userVerifyCode)){
            return ResultUtil.errorOperation("验证码错误，请重新输入");
        }
        User userSession= (User) session.getAttribute(SysConstant.CURRENT_USER);
        user=userService.queryUserByEmail(userSession.getUserEmail());
        user.setUserPhone(userPhone);
        session.setAttribute(SysConstant.CURRENT_USER,user);
        return ResultUtil.actionSuccess("绑定手机成功",user);
    }

    /**
     * 方法实现说明 展现学生个人信息
     * @author      lxy
     * @Param:
     * @return
     * @exception
     * @date        2019/4/24 16:17
     */
    @RequestMapping("/showStudentInfo")
    @ResponseBody
    public Result showStudentInfo(User user, HttpSession session){
        User userSession=(User)session.getAttribute(SysConstant.CURRENT_USER);
        if (userSession == null){
            return ResultUtil.errorOperation("展现信息失败");
        }
        if (userSession.getUserPhone()==null){
            user=userService.queryUserByEmail(userSession.getUserEmail());
            return ResultUtil.actionSuccess("展现信息成功",user);
        }
        user=userService.queryUserByPhone(userSession.getUserPhone());
        return ResultUtil.actionSuccess("展现信息成功",user);
    }


/**
 * 方法实现说明  完善学生信息,user直接从前端传过来
 * @author      lxy
 * @Param:
 * @return
 * @exception
 * @date        2019/4/24 15:52
 */

@RequestMapping("/updateStudentInfo")
@ResponseBody
public Result updateStudentInfo(User user,HttpSession session){
            if (user.getRealName().equals("") || user.getIdcard().equals("")){
                return ResultUtil.errorOperation("请完善身份证信息和真实名字");
            }
            if (!user.getIdcard().matches(RegexpUtil.RegExp_ID)){
                return ResultUtil.errorOperation("身份证格式不匹配，请重新输入");
            }
            if (!user.getUserEmail().matches(RegexpUtil.RegExp_Mail)){
                return ResultUtil.errorOperation("邮箱格式不匹配，请重新输入");
            }
            User exit=userService.queryUserByEmail(user.getUserEmail());
            if (exit!=null){
                return ResultUtil.errorOperation("该邮箱已经被绑定，请重新输入");
            }

            User userReal=userService.queryUserByEmail(user.getUserEmail());
            if (userReal==null){
                userReal=userService.queryUserByPhone(user.getUserPhone());
            }
            userReal.setIdcard(user.getIdcard());
            userReal.setUserNickname(user.getUserNickname());
            userReal.setUserEmail(user.getUserEmail());
            userReal.setUserPhone(user.getUserPhone());
            userReal.setUserHeadimg(user.getUserHeadimg());
            userReal.setUserPrivacy(user.getUserPrivacy());
            userReal.setRealName(user.getRealName());
            userReal.setSex(user.getSex());
            userReal.setUserQq(user.getUserQq());
            userReal.setUserWechat(user.getUserWechat());
            userReal.setUserLocation(user.getUserLocation());
            return ResultUtil.actionSuccess("成功完善信息",userReal);
    }
    /**
     * 方法实现说明 展现教练个人信息
     * @author      lxy
     * @Param:
     * @return
     * @exception
     * @date        2019/4/24 16:17
     */
    @RequestMapping("/showCoachInfo")
    @ResponseBody
    public Map<String,Object> showCoachInfo(User user,Coach coach, String info,HttpSession session){
        Map<String,Object> result=new HashMap<>();
        User userSession=(User)session.getAttribute(SysConstant.CURRENT_USER);
        if (userSession == null){
            info="展现信息失败";
            result.put(SysConstant.CURRENT_MESSAGE,info);
            return result;
        }
        if (userSession.getUserPhone().equals("")){
            user=userService.queryUserByEmail(userSession.getUserEmail());
        }
        user=userService.queryUserByPhone(userSession.getUserPhone());
        coach=coachService.findCoachByUserId(user.getUserId());
        info="展现信息成功";
        result.put(SysConstant.CURRENT_MESSAGE,info);
        result.put(SysConstant.CURRENT_USER,user);
        result.put(SysConstant.CURRENT_COACH,coach);
        return result;
    }


    /**
     * 方法实现说明  完善教练信息,user直接从前端传过来
     * @author      lxy
     * @Param:
     * @return
     * @exception
     * @date        2019/4/24 15:52
     */

    @RequestMapping("/updateCoachInfo")
    @ResponseBody
    public Map<String,Object> updateCoachInfo(User user,HttpSession session,Coach coach,String info){
        User userSession= (User) session.getAttribute(SysConstant.CURRENT_USER);
        Map<String,Object> result=new HashMap<>();
        if (!user.getUserEmail().equals("")){
            if (!user.getUserEmail().matches(RegexpUtil.RegExp_Mail)){
                info="邮箱格式不匹配，请重新输入";
                result.put(SysConstant.CURRENT_MESSAGE,info);
                return result;
            }
            User exit=userService.queryUserByEmail(user.getUserEmail());
            if (exit!=userService.queryUserByEmail(userSession.getUserEmail())&& exit!=null){
                info="该邮箱已经被绑定，请重新输入";
                result.put(SysConstant.CURRENT_MESSAGE,info);
                return result;
            }
        }
        if (user.getRealName().equals("") || user.getIdcard().equals("")){
            info="请完善身份证信息和真实名字";
            result.put(SysConstant.CURRENT_MESSAGE,info);
            return result;
        }
        if (!user.getIdcard().matches(RegexpUtil.RegExp_ID)){
            info="身份证格式不匹配，请重新输入";
            result.put(SysConstant.CURRENT_MESSAGE,info);
            return result;
        }
        User userReal=userService.queryUserByEmail(user.getUserEmail());
        if (userReal==null){
            userReal=userService.queryUserByPhone(user.getUserPhone());
        }

        userReal.setIdcard(user.getIdcard());
        userReal.setUserNickname(user.getUserNickname());
        userReal.setUserEmail(user.getUserEmail());
        userReal.setUserPhone(user.getUserPhone());
        userReal.setUserHeadimg(user.getUserHeadimg());
        userReal.setUserPrivacy(user.getUserPrivacy());
        userReal.setRealName(user.getRealName());
        userReal.setSex(user.getSex());
        userReal.setUserQq(user.getUserQq());
        userReal.setUserWechat(user.getUserWechat());
        userReal.setUserLocation(user.getUserLocation());
        Coach coachReal=coachService.findCoachByUserId(userReal.getUserId());
        if (coachReal==null){
            info="系统错误，请联系管理员";
            result.put(SysConstant.CURRENT_MESSAGE,info);
            return result;
        }

        coachReal.setCoachStyle(coach.getCoachStyle());
        coachReal.setAuthentication(coach.getAuthentication());
        coachReal.setCoachStatus(coach.getCoachStatus());
        coachReal.setFullClass(coach.getFullClass());
        coachReal.setCoachType(coach.getCoachType());
        coachReal.setStartTime(coach.getStartTime());
        coachReal.setCoachDetail(coach.getCoachDetail());
        coachReal.setExpectedSalary(coach.getExpectedSalary());
        session.setAttribute(SysConstant.CURRENT_USER,userReal);
        session.setAttribute(SysConstant.CURRENT_COACH,coachReal);
        info="成功完善信息";
        result.put(SysConstant.CURRENT_COACH,coachReal);
        result.put(SysConstant.CURRENT_MESSAGE,info);
        result.put(SysConstant.CURRENT_USER,userReal);
        return result;
    }
    /**
     * 方法实现说明 文件上传
     * @author      lxy
     * @Param:
     * @return
     * @exception
     * @date        2019/4/26 1:12
     */
    @RequestMapping(value = "/uploadHead",method = RequestMethod.POST)
    @ResponseBody
    public Result uploadHead(@RequestParam("userHeadimg")MultipartFile userHeadimg, HttpServletRequest request, HttpSession session,
                             User user) throws IOException {
        System.out.println(user+"sssssss"+userHeadimg);
        if (userHeadimg==null){
            System.out.println("请上传文件");
            return ResultUtil.errorOperation("上传失败");
        }
        System.out.println(userHeadimg);
        String fileName = userHeadimg.getOriginalFilename();
        String filetype = userHeadimg.getContentType();
        String path = request.getServletContext().getRealPath("/img");
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName = UUID.randomUUID().toString() + fileName;
        path = path + File.separator + fileName;
        file = new File(path);
        userHeadimg.transferTo(file);
        user.setUserHeadimg(fileName);
        System.out.println("上传成功");
        return ResultUtil.actionSuccess("上传成功",user);
    }

    /**
     * 方法实现说明  展现个人隐私
     * @author      lxy
     * @Param:
     * @return
     * @exception
     * @date        2019/4/27 21:07
     */
    @RequestMapping("/showPrivacy")
    @ResponseBody
    public Result showPrivacy(User user, HttpSession session){
        User userSession=(User)session.getAttribute(SysConstant.CURRENT_USER);
        if (userSession == null){
            return ResultUtil.errorOperation("展现信息失败");
        }
        if (userSession.getUserPhone()==null){
            user=userService.queryUserByEmail(userSession.getUserEmail());
            return ResultUtil.actionSuccess("展现信息成功",user);
        }
        user=userService.queryUserByPhone(userSession.getUserPhone());
        return ResultUtil.actionSuccess("展现信息成功",user);
    }
    /**
     * 方法实现说明 修改隐私
     * @author      lxy
     * @Param:
     * @return
     * @exception
     * @date        2019/4/27 21:11
     */
    @RequestMapping("/updatePrivacy")
    @ResponseBody
    public Result updatePrivacy(User user,HttpSession session){
        User userSession= (User) session.getAttribute(SysConstant.CURRENT_USER);
        User userReal=userService.queryUserByEmail(userSession.getUserEmail());
        if (userReal==null){
            userReal=userService.queryUserByPhone(userSession.getUserPhone());
        }
        userReal.setUserPrivacy(user.getUserPrivacy());
        if(!userReal.getUserPrivacy().equals(user.getUserPrivacy())){
            return ResultUtil.errorOperation("修改失败");
        }
        return ResultUtil.actionSuccess("修改成功",userReal);
    }
    /**
     * 方法实现说明 展现账户与安全
     * @author      lxy
     * @Param:
     * @return
     * @exception
     * @date        2019/4/29 10:56
     */
    @RequestMapping("/showAccount")
    @ResponseBody
    public Result showAccount(User user, HttpSession session){
        User userSession=(User)session.getAttribute(SysConstant.CURRENT_USER);
        if (userSession == null){
            return ResultUtil.errorOperation("展现信息失败");
        }
        if (userSession.getUserPhone()==null){
            user=userService.queryUserByEmail(userSession.getUserEmail());
            return ResultUtil.actionSuccess("展现信息成功",user);
        }
        user=userService.queryUserByPhone(userSession.getUserPhone());
        return ResultUtil.actionSuccess("展现信息成功",user);
    }
    /**
     * 方法实现说明 展现设置密码
     * @author      lxy
     * @Param:
     * @return
     * @exception
     * @date        2019/4/29 10:58
     */
    @RequestMapping("/showModifyUserPwd")
    @ResponseBody
    public Result showModifyUserPwd(User user, HttpSession session){
        User userSession=(User)session.getAttribute(SysConstant.CURRENT_USER);
        if (userSession == null){
            return ResultUtil.errorOperation("展现信息失败");
        }
        if (userSession.getUserPhone()==null){
            user=userService.queryUserByEmail(userSession.getUserEmail());
            return ResultUtil.actionSuccess("展现信息成功",user);
        }
        user=userService.queryUserByPhone(userSession.getUserPhone());
        return ResultUtil.actionSuccess("展现信息成功",user);
    }



    /**
     * 方法实现说明 修改密码
     * @author      lxy
     * @Param:
     * @return
     * @exception
     * @date        2019/4/29 10:57
     */
    @RequestMapping("/modifyUserPwd")
    @ResponseBody
    public Result modifyUserPwd(User user,HttpSession session,String confirmPwd,
                                String userNewPwd,String userOldPwd){
        User userSession= (User) session.getAttribute(SysConstant.CURRENT_USER);
        User userReal=userService.queryUserByEmail(userSession.getUserEmail());
        if (userReal==null){
            userReal=userService.queryUserByPhone(userSession.getUserPhone());
        }
        System.out.println(userReal.getUserPwd());
        SimpleHash userHashOldPwd = new SimpleHash("MD5",userOldPwd,userReal.getSalt(),2);

        if (!userHashOldPwd.toString().equals(userReal.getUserPwd())){
            return  ResultUtil.errorOperation("输入的旧密码不正确，请重新输入");
        }
        if (userOldPwd.equals(userNewPwd)){
            return ResultUtil.errorOperation("输入的新密码与旧密码不能相同，请重新输入");
        }
        if (!userNewPwd.equals(confirmPwd)){
            return ResultUtil.errorOperation("输入的新密码与确认密码不同，请重新输入");
        }
        userReal.setSalt(CodeUtil.userNumber());
        SimpleHash userHashNewPwd = new SimpleHash("MD5",userNewPwd,userReal.getSalt(),2);
        userReal.setUserPwd(userHashNewPwd.toString());
        if (!userReal.getUserPwd().equals(userHashNewPwd.toString())){
            return ResultUtil.connectDatabaseFail();
        }
        return ResultUtil.actionSuccess("修改成功",userReal);
    }



}