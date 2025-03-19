package com.david.hlp.SpringBootWork.system.service;

import org.springframework.stereotype.Service;
import com.david.hlp.SpringBootWork.system.mapper.RoleMapper;
import com.david.hlp.SpringBootWork.system.entity.role.Role;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.david.hlp.SpringBootWork.system.entity.permission.Permission;
import com.david.hlp.SpringBootWork.system.mapper.PermissionMapper;
import com.david.hlp.SpringBootWork.system.mapper.RouterMapper;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import com.david.hlp.SpringBootWork.system.entity.role.RolePermissionUpdateResponse;
import java.util.ArrayList;
import com.david.hlp.SpringBootWork.system.entity.role.RolePermission;
@Service
@RequiredArgsConstructor
public class RoleServiceImp {
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final RouterMapper routerMapper;

    public Role getRole(Long roleId) {
        return roleMapper.findRoleByRoleId(roleId);
    }

    public List<Role> getRoleList(String roleName) {
        return roleMapper.findAll(roleName);
    }

    public List<Role> getRoleList() {
        List<Role> roleList = roleMapper.findAll(null);
        for (Role role : roleList) {
            role.setPermissions(permissionMapper.findAllByRoleId(role.getId()));
            role.setRouters(routerMapper.findAllByPermissionName(role.getPermissions().stream().map(Permission::getPermission).collect(Collectors.toList())));
        }
        return roleList;
    }
    @Transactional(rollbackFor = Exception.class)
    public void addRole(Role role) {
        roleMapper.insertRole(role);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editRole(Role role) {
        roleMapper.updateRole(role);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRolePermissions(RolePermissionUpdateResponse rolePermissionUpdateResponse) {
        Long roleId = rolePermissionUpdateResponse.getRoleId();
        // 1. 获取现有的角色权限
        List<RolePermission> existingPermissions = roleMapper.findRolePermissions(roleId);
        List<Long> existingPermissionIds = existingPermissions.stream()
            .map(RolePermission::getPermissionId)
            .collect(Collectors.toList());
        // 2. 获取新的权限ID列表
        List<Long> newPermissionIds = new ArrayList<>();

        if (!rolePermissionUpdateResponse.getRouterIds().isEmpty()) {
            for (Integer routerId : rolePermissionUpdateResponse.getRouterIds()) {
                String permission = routerMapper.findPermissionNameByRouterId(routerId.longValue());
                if (permission != null) {
                    Long permissionId = permissionMapper.findPermissionIdByName(permission);
                    if (permissionId != null) {
                        newPermissionIds.add(permissionId);
                    }
                }
            }
        }
        // 3. 找出需要删除的权限ID (在旧列表中有但新列表中没有)
        List<Long> toDeletePermissionIds = existingPermissionIds.stream()
            .filter(id -> !newPermissionIds.contains(id))
            .collect(Collectors.toList());
        // 4. 找出需要添加的权限ID (在新列表中有但旧列表中没有)
        List<Long> toAddPermissionIds = newPermissionIds.stream()
            .filter(id -> !existingPermissionIds.contains(id))
            .distinct() // 确保没有重复
            .collect(Collectors.toList());
        // 5. 执行删除操作
        if (!toDeletePermissionIds.isEmpty()) {
            for (Long permissionId : toDeletePermissionIds) {
                roleMapper.deleteRolePermission(roleId, permissionId);
            }
        }
        // 6. 执行添加操作
        if (!toAddPermissionIds.isEmpty()) {
            roleMapper.insertRolePermissions(roleId, toAddPermissionIds);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long roleId) {
        roleMapper.deleteRolePermissionByRoleId(roleId);
        roleMapper.deleteRoleByRoleId(roleId);
    }
}
