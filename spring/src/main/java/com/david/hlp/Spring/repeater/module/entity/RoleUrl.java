package com.david.hlp.Spring.repeater.module.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
/**
 * 角色信息实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RoleUrl {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 项目ID
     */
    private Long projectId;
    
    /**
     * 文档说明
     */
    private String doc;
    
    /**
     * 创建时间
     */
    private Date gmtCreate;
    
    /**
     * 修改时间
     */
    private Date gmtModified;
    
    /**
     * 是否删除:0-未删除,1-已删除
     */
    private Integer isDeleted;

    /**
     * 项目信息
     */
    private Project project;

}