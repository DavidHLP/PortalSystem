package com.david.hlp.Spring.repeater.module.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 用户角色项目关联实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserRoleProject {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 状态
     */
    private Integer status;
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 角色ID
     */
    private Long roleId;
    
    /**
     * 项目ID
     */
    private Long projectId;
    
    /**
     * 项目名称
     */
    private String projectName;
    
    /**
     * 项目文档
     */
    private String projectDoc;
    
    /**
     * 角色文档
     */
    private String roleDoc;
}
