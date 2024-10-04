package com.github.xioshe.less.url.repository;

import com.github.xioshe.less.url.entity.Permission;
import com.github.xioshe.less.url.repository.mapper.PermissionMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PermissionRepository extends BaseRepository<PermissionMapper, Permission> {

    public Optional<Permission> findByCode(String code) {
        return lambdaQuery().eq(Permission::getCode, code).oneOpt();
    }
}
