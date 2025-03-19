package com.david.hlp.SpringBootWork.system.service;

import com.david.hlp.SpringBootWork.system.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import com.david.hlp.SpringBootWork.system.entity.auth.AuthUser;
import com.david.hlp.SpringBootWork.system.mapper.RoleMapper;
import lombok.extern.slf4j.Slf4j;
import com.david.hlp.SpringBootWork.system.entity.role.Role;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthUser user = userMapper.findByEmailToAuthUser(email);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + email);
        }
        Role role = roleMapper.findRoleByRoleId(user.getRoleId());
        List<String> authorities = new ArrayList<>(userMapper.findAuthoritiesByRoleId(role.getId()));
        log.info("用户 {} 的完整权限列表：{}", user.getUsername(), authorities);
        user.setAuthorities(authorities);
        return user;
    }
}