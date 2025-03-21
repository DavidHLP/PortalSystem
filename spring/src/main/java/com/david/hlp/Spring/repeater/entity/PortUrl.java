package com.david.hlp.Spring.repeater.entity;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 端口URL实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PortUrl {
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 端口号（支持数字或字符串，如8080/ws）
     */
    private String number;
    
    /**
     * 端口用途说明（如HTTP/WebSocket）
     */
    private String description;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
} 