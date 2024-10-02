package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.entity.Role;
import com.github.xioshe.less.url.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    public List<Role> listEnabledRolesByUserId(Long userId) {
        List<Role> roles = roleRepository.listEnabledRolesByUserId(userId);
        for (Role role : roles) {
            role.setPermissions(permissionService.listPermissionsByRoleId(role.getId()));
        }
        return roles;
    }

    public void assignPermissionToRole(Long roleId, Long permissionId) {

    }
}
