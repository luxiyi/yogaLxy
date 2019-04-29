package com.woniu.yago.realm;

import com.woniu.yago.constant.SysConstant;
import com.woniu.yago.pojo.Role;
import com.woniu.yago.pojo.User;
import com.woniu.yago.service.RoleService;
import com.woniu.yago.service.UserService;
import com.woniu.yago.utils.RegexpUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class AppPwdRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("开始授权");
        User user= (User) principals.getPrimaryPrincipal();
        Set<String> roles=new HashSet<>();
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo(roles);
        if (user == null){
            return info;
        }
        Role role=roleService.findByRoleId(user.getRoleId());
        roles.add(role.getRoleName());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("开始认证");
        String userName=(String)token.getPrincipal();
        User user=new User();
        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo();
        if (userName.matches(RegexpUtil.RegExp_PHONE)){
            System.out.println("手机");
            System.out.println("xxxxxxxxxxxxxxxxxxxxxx");
            user=userService.queryUserByPhone(userName);
            info=new SimpleAuthenticationInfo(user,user.getUserPwd(),this.getName());

        }
        if (userName.matches(RegexpUtil.RegExp_Mail)){
            System.out.println("邮箱");
            user=userService.queryUserByEmail(userName);
            info=new SimpleAuthenticationInfo(user,user.getUserPwd(),this.getName());
        }
        if (userName == null || userName.equals("")){
            throw new UnknownAccountException("账户错误");
        }
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(SysConstant.CURRENT_USER,user);
        info.setCredentialsSalt(ByteSource.Util.bytes(user.getSalt()));
        return info;
    }

    public static void main(String[] args) {

        ByteSource source = ByteSource.Util.bytes("hello".getBytes());
        System.out.println(new SimpleHash("MD5", "lxy1234", source, 2));
    }
}
