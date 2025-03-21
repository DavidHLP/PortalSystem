package com.david.hlp.Spring.repeater.entity;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 路由URL实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouterUrl {
    /**
     * 主键ID
     */
    private Integer id;
    
    /**
     * URL路径（支持RESTful参数，如/api/v1/:id）
     */
    private String path;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
} 