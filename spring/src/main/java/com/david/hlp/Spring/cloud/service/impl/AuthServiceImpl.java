package com.david.hlp.Spring.cloud.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.david.hlp.Spring.cloud.mapper.AuthMapper;
import com.david.hlp.Spring.cloud.module.entity.LoginRequest;
import com.david.hlp.Spring.cloud.module.entity.CloudToken;
import com.david.hlp.Spring.common.enums.TokenType;
import com.david.hlp.Spring.common.service.JwtCommonService;
import com.david.hlp.Spring.common.util.RedisCache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {
    private final AuthMapper authMapper;
    @Qualifier("BCryptPasswordEncoder")
    private final PasswordEncoder passwordEncoder;
    private final JwtCommonService<LoginRequest> jwtCommonService;
    private final RedisCache redisCache;
    private final String prefix = "token:email:";

    /**
     * 登录
     *
     * @param loginRequest 登录请求
     * @return LoginRequest 登录请求
     */
    public CloudToken login(LoginRequest loginRequest) {
        LoginRequest user = authMapper.login(loginRequest);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        CloudToken cloudToken = createToken(user);
        redisSaveToken(cloudToken);
        return cloudToken;
    }

    /**
     * 创建令牌
     *
     * @param loginRequest 登录请求
     * @return CloudToken 令牌
     */
    public CloudToken createToken(LoginRequest loginRequest) {
        String token = jwtCommonService.generateToken(loginRequest);
        return CloudToken.builder()
                .email(loginRequest.getEmail())
                .username(loginRequest.getUsername())
                .tokenType(TokenType.BEARER)
                .token(token)
                .build();
    }

    public CloudToken refreshToken(CloudToken cloudToken) {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(cloudToken.getEmail())
                .username(cloudToken.getUsername())
                .build();
        String token = jwtCommonService.generateToken(loginRequest);
        return CloudToken.builder()
                .email(cloudToken.getEmail())
                .username(cloudToken.getUsername())
                .tokenType(TokenType.BEARER)
                .token(token)
                .build();
    }

    public void redisSaveToken(CloudToken cloudToken) {
        redisCache.setCacheObject(prefix + cloudToken.getEmail() + ":" + cloudToken.getToken(), cloudToken,
                jwtCommonService.getJwtExpiration(), TimeUnit.MILLISECONDS);
    }

    public void redisDeleteToken(CloudToken cloudToken) {
        redisCache.deleteObject(prefix + cloudToken.getEmail() + ":" + cloudToken.getToken());
    }

    public CloudToken redisReadToken(CloudToken cloudToken) {
        return redisCache.getCacheObject(prefix + cloudToken.getEmail() + ":" + cloudToken.getToken());
    }

    public List<CloudToken> redisReadAllToken(CloudToken cloudToken) {
        return redisCache.getCacheList(prefix + cloudToken.getEmail() + ":");
    }

    public void redisDeleteAllToken(CloudToken cloudToken) {
        redisCache.deleteObject(prefix + cloudToken.getEmail() + ":");
    }

    public Boolean checkToken(CloudToken cloudToken) {
        return redisReadToken(cloudToken) != null;
    }
}
