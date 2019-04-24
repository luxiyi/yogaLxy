package com.woniu.yago.dao;

import com.woniu.yago.pojo.Role;

/**
 * @Description: java类作用描述RoleMapper
 * @Author: lxy
 * @time: 2019/4/24 2:15
 */
public interface RoleMapper {
        Role findUserByRoleId(Integer roleId);
}
