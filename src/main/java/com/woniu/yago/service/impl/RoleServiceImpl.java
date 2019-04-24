package com.woniu.yago.service.impl;

import com.woniu.yago.dao.RoleMapper;
import com.woniu.yago.pojo.Role;
import com.woniu.yago.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: java类作用描述RoleServiceImpl
 * @Author: lxy
 * @time: 2019/4/24 2:14
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;


    @Override
    public Role findRoleByRoleId(Integer roleId) {
        return roleMapper.findUserByRoleId(roleId);
    }
}
