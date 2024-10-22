package com.github.xioshe.less.url.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.xioshe.less.url.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> listEnabledRolesByUserId(Long userId);

    int assignRoleToUser(@Param("userId")Long userId, @Param("roleId")Long roleId);
}