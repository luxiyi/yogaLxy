package com.woniu.yago.dao.impl;

import com.woniu.yago.dao.RoleMapper;
import com.woniu.yago.pojo.Role;
import com.woniu.yago.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @Description: java类作用描述RoleMapperImpl
 * @Author: lxy
 * @time: 2019/4/24 2:19
 */
@Repository
public class RoleMapperImpl implements RoleMapper {
    @Autowired
    private RoleRepository roleRepository;


    @Override
    public Role findUserByRoleId(Integer roleId) {
        return roleRepository.findUserByRoleId(roleId);
    }
}
