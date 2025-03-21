package com.david.hlp.Spring.repeater.entity;

import lombok.Builder;
import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 项目URL实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUrl {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 项目名称，唯一
     */
    @Builder.Default
    private String projectName = "";
    /**
     * 项目接口名称
     */
    @Builder.Default
    private String projectInterfaceName = "";
    /**
     * 项目文档
     */
    private String description;
}