package com.david.hlp.Spring.repeater.module.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 路由信息实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouterUrl {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 主机地址
     */
    private String host;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 路由路径
     */
    private String router;

    /**
     * 协议类型
     */
    private String protocol;

    /**
     * 唯一标识
     */
    private String uniqueId;

    /**
     * 路由类型:0-内部,1-外部
     */
    private Integer type;

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