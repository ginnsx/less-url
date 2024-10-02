package com.github.xioshe.less.url.repository;

import com.github.xioshe.less.url.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionRepository {
    int deleteByPrimaryKey(Long id);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

    Permission findByCode(String code);

    List<Permission> listEnabledPermissionsByRoleId(Long roleId);
}