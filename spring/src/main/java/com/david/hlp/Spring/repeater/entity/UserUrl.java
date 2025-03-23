package com.david.hlp.Spring.repeater.entity;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户URL实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUrl {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 登录用户名
     */
    private String username;
    /**
     * SHA256加密的密码
     */
    private String passwordHash;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 关联角色ID
     */
    private Integer roleId;

    /**
     * 关联角色对象
     */
    private RoleUrl role;

    /**
     * 关联项目ID
     */
    private Integer projectId;

    /**
     * 关联项目对象
     */
    private ProjectUrl project;

    /**
     * 账户状态
     */
    private Boolean isActive;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}