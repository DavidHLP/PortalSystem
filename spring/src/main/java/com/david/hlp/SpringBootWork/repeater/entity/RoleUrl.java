package com.david.hlp.SpringBootWork.repeater.entity;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 角色URL实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleUrl {
    /**
     * 主键ID
     */
    private Integer id;
    
    /**
     * 关联项目ID
     */
    private Integer projectId;
    
    /**
     * 关联项目对象
     */
    private ProjectUrl project;
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 角色描述
     */
    private String description;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
} 