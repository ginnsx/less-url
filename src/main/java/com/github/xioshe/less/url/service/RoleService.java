package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.entity.Role;
import com.github.xioshe.less.url.repository.RoleRepository;
import com.github.xioshe.less.url.repository.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleMapper roleMapper;
    private final PermissionService permissionService;
    private final RoleRepository roleRepository;

    public List<Role> listEnabledRolesByUserId(Long userId) {
        List<Role> roles = roleMapper.listEnabledRolesByUserId(userId);
        for (Role role : roles) {
            role.setPermissions(permissionService.listPermissionsByRoleId(role.getId()));
        }
        return roles;
    }

    public void assignPermissionToRole(Long roleId, Long permissionId) {

    }

    public void assignRoleToUser(Long userId, String... roles) {
        for (String role : roles) {
            var roleId = roleRepository.lambdaQuery()
                    .select(Role::getId).eq(Role::getCode, role).oneOpt()
                    .map(Role::getId)
                    .orElseThrow();
            roleMapper.assignRoleToUser(userId, roleId);
        }
    }
}
