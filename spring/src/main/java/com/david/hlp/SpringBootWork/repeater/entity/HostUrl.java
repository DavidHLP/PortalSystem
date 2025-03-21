package com.david.hlp.SpringBootWork.repeater.entity;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 主机URL实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HostUrl {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 域名或IP地址（支持IPv4/IPv6格式）
     */
    private String address;
    /**
     * 主机用途说明
     */
    private String description;
    /**
     * 状态标识（true:启用 false:停用）
     */
    private Boolean isActive;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
} 