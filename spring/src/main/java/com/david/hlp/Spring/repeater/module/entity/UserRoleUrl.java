package com.david.hlp.Spring.repeater.module.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户角色关联实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleUrl {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;

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

}