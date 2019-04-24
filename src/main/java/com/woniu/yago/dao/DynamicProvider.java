package com.woniu.yago.dao;

import com.woniu.yago.pojo.User;
import org.apache.ibatis.jdbc.SQL;

/**
 * @Description: java类作用描述DynamicProvider
 * @Author: lxy
 * @time: 2019/4/23 0:57
 */
public class DynamicProvider {
    public String queryUserByPhoneOrEmail(User user){
        SQL sql=new SQL().SELECT("*").FROM("user");
        if (user.getUserEmail()!=null && user.getUserEmail().length()!=0){
            sql.WHERE("userEmail ='"+user.getUserEmail()+"'");
        }
        if (user.getUserPhone()!=null && user.getUserPhone().length()!=0){
            sql.WHERE("userPhone ='"+user.getUserPhone()+"'");
        }
        return sql.toString();
    }
}
