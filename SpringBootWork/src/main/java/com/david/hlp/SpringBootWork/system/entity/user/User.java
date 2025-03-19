package com.david.hlp.SpringBootWork.system.entity.user;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;             // BIGINT 主键
    private String name;         // 用户名
    private String avatar;       // 头像
    @Default
    private String introduction = "用户未填写";      // 简介
    private String email;        // 邮箱（唯一）
    private Integer status;      // 用户状态
    @Default
    private String address = "用户未填写";      // 地址
    private String lastLoginIp;  // 上次登录 IP
    private LocalDateTime lastLogin;   // 上次登录时间
    private Long roleId;         // 角色 ID
    private String roleName;     // 角色名称
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createTime;  // 创建时间
    private String password;     // 加密后的密码

    public Long getUserId() {
        return id;
    }

    public String getUsername() {
        return name;
    }
}