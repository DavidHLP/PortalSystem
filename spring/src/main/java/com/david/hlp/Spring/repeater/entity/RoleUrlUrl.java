package com.david.hlp.Spring.repeater.entity;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 角色-URL权限关联实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleUrlUrl {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 关联角色ID
     */
    private Integer roleId;
    /**
     * 关联角色对象
     */
    private RoleUrl role;
    /**
     * 关联URL ID
     */
    private Integer urlId;
    /**
     * 关联URL对象
     */
    private Url url;
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}