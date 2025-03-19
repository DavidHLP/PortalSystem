package com.david.hlp.SpringBootWork.system.entity.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Spring Security 用的用户类，实现 UserDetails 接口。
 */
@Data
@Builder
@AllArgsConstructor
public class AuthUser implements UserDetails {

    private final Long userId;               // 数据库中的用户主键ID
    private final String username;           // 登录名（在此示例中使用 email 或其他字段）
    private final String email;              // 邮箱
    private final String password;           // 加密后的密码
    private final Integer status;            // 用户状态：0-禁用，1-启用
    private final Long roleId;              // 角色ID
    private List<String> authorities;

    public AuthUser(
        Long userId,
        String username,
        String password,
        Integer status,
        Long roleId,
        String email
    ) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.status = status;
        this.roleId = roleId;
        this.email = email;
    }


    // 可选：如果需要在应用中获取 userId
    public Long getUserId() {
        return userId;
    }
    /**
     * 返回用户拥有的权限（角色/权限标识）
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * 返回用户密码
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 返回用户登录名
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * 账户是否未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        // 可根据业务需求进行定制
        return true;
    }

    /**
     * 账户是否未被锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        // 可根据业务需求进行定制
        return true;
    }

    /**
     * 凭证是否未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        // 可根据业务需求进行定制
        return true;
    }

    /**
     * 账户是否可用
     */
    @Override
    public boolean isEnabled() {
        // 当 status 为 0 表示禁用，1 表示启用
        return (status != null && status == 1);
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
}