package com.david.hlp.Spring.repeater.module.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目信息实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 项目名称
     */
    private String projectName;

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
}