package com.woniu.yago.service.impl;

import com.woniu.yago.pojo.Role;
import com.woniu.yago.repository.RoleRepository;
import com.woniu.yago.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * @Description: java类作用描述RoleServiceImpl
 * @Author: lxy
 * @time: 2019/4/24 2:14
 */
@Service
@Repository
public class RoleServiceImpl implements RoleService {
//    @Autowired
//    private RoleMapper roleMapper;
    @Autowired
    private RoleRepository roleRepository;


    @Override
    public Role findByRoleId(Integer roleId) {
        return roleRepository.findByRoleId(roleId);
    }

    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
