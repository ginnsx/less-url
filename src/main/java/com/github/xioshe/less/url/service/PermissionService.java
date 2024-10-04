package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.entity.Permission;
import com.github.xioshe.less.url.repository.PermissionRepository;
import com.github.xioshe.less.url.repository.mapper.PermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionMapper permissionMapper;
    private final PermissionRepository permissionRepository;

    public List<Permission> listPermissionsByRoleId(Long roleId) {
        return permissionMapper.listEnabledPermissionsByRoleId(roleId);
    }

    public Permission createPermissionIfNotExists(String code, String name, String description) {
        return permissionRepository.findByCode(name).orElseGet(() -> {
            Permission permission = new Permission();
            permission.setCode(code);
            permission.setName(name);
            permission.setDescription(description);
            permission.setEnabled(true);
            permissionMapper.insert(permission);
            return permission;
        });
    }
}
