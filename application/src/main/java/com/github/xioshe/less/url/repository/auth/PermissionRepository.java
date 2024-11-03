package com.github.xioshe.less.url.repository.auth;

import com.github.xioshe.less.url.entity.auth.Permission;
import com.github.xioshe.less.url.mapper.PermissionMapper;
import com.github.xioshe.less.url.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PermissionRepository extends BaseRepository<PermissionMapper, Permission> {

    public Optional<Permission> findByCode(String code) {
        return lambdaQuery().eq(Permission::getCode, code).oneOpt();
    }
}
