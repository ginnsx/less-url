package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.entity.Permission;
import com.github.xioshe.less.url.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public List<Permission> listPermissionsByRoleId(Long roleId) {
        return permissionRepository.listEnabledPermissionsByRoleId(roleId);
    }

    @Transactional
    public Permission createPermissionIfNotExists(String code, String name, String description) {
        Permission permission = permissionRepository.findByCode(name);
        if (permission == null) {
            permission = new Permission();
            permission.setCode(code);
            permission.setName(name);
            permission.setDescription(description);
            permission.setEnabled(true);
            permissionRepository.insertSelective(permission);
        }
        return permission;
    }
}
