package com.david.hlp.SpringBootWork.system.entity.role;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RolePermission {
    private Long roleId;       // 角色ID
    private Long permissionId; // 权限ID
}