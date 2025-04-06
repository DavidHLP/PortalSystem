package com.david.hlp.Spring.cloud.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.david.hlp.Spring.cloud.module.entity.LoginRequest;

@Mapper
public interface AuthMapper {
    /**
     * 登录
     * @param loginRequest
     * @return LoginRequest 登录请求
     */
    LoginRequest login(LoginRequest loginRequest);
}
