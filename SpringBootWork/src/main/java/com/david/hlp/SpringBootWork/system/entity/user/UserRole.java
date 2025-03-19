package com.david.hlp.SpringBootWork.system.entity.user;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {
    private Long userId; // 用户ID
    private Long roleId; // 角色ID
} 