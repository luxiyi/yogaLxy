package com.woniu.yago.repository;

import com.woniu.yago.pojo.Role;
import com.woniu.yago.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: java类作用描述RoleRepository
 * @Author: lxy
 * @time: 2019/4/24 2:21
 */
@Transactional
public interface RoleRepository extends JpaRepository<User,Integer> {
        Role findUserByRoleId(Integer roleId);
}
