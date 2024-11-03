package com.github.xioshe.less.url.service.auth;

import com.github.xioshe.less.url.entity.auth.Permission;
import com.github.xioshe.less.url.repository.auth.PermissionRepository;
import com.github.xioshe.less.url.mapper.PermissionMapper;
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
