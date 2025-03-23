package com.david.hlp.Spring.repeater.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.david.hlp.Spring.system.token.TokenType;

import java.time.LocalDateTime;

/**
 * 用户认证令牌实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenUrl {

    /**
     * 主键ID，自增
     */
    private Long id;

    /**
     * 用户ID，外键关联user表
     */
    private Long userId;

    /**
     * Token值
     */
    private String token;

    /**
     * 令牌类型
     */
    private TokenType tokenType;

    /**
     * 是否过期
     */
    @Builder.Default
    private Boolean expired = false;

    /**
     * 是否撤销
     */
    @Builder.Default
    private Boolean revoked = false;

    /**
     * 创建时间
     */
    @Builder.Default
    private LocalDateTime createTime = LocalDateTime.now();

    /**
     * 更新时间
     */
    @Builder.Default
    private LocalDateTime updateTime = LocalDateTime.now();
}
