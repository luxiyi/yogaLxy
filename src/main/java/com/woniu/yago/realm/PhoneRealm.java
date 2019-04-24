package com.woniu.yago.realm;

import com.woniu.yago.com.woniu.yago.vo.PhoneToken;
import com.woniu.yago.pojo.User;
import com.woniu.yago.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: java类作用描述PhoneRealm
 * @Author: lxy
 * @time: 2019/4/23 16:13
 */
public class PhoneRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("手机认证");
        PhoneToken token = null;
        // 如果是PhoneToken，则强转，获取phone；否则不处理。
        if(authenticationToken instanceof PhoneToken){
            token = (PhoneToken) authenticationToken;
        }else{
            return null;
        }
        String userPhone = (String) token.getPrincipal();
        User user = userService.queryUserByPhone(userPhone);
        if (user == null) {
            System.out.println("手机号不正确");
            throw new UnknownAccountException("手机号不正确");
        }
        return new SimpleAuthenticationInfo(user, userPhone, this.getName());
    }
    @Override
    public boolean supports(AuthenticationToken var1){
        return var1 instanceof PhoneToken;
    }
}
