package com.david.hlp.SpringBootWork.system.service;

import org.springframework.stereotype.Service;
import com.david.hlp.SpringBootWork.system.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.david.hlp.SpringBootWork.common.enums.ResultCode;
import com.david.hlp.SpringBootWork.system.auth.JwtService;
import com.david.hlp.SpringBootWork.system.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import com.david.hlp.SpringBootWork.system.entity.user.User;
import com.david.hlp.SpringBootWork.system.entity.auth.RegistrationRequest;
import org.springframework.transaction.annotation.Transactional;
import com.david.hlp.SpringBootWork.system.token.Token;
import com.david.hlp.SpringBootWork.system.entity.auth.LoginRequest;
import com.david.hlp.SpringBootWork.common.exception.BusinessException;
import com.david.hlp.SpringBootWork.system.entity.auth.AuthUser;
import com.david.hlp.SpringBootWork.system.token.TokenType;
import com.david.hlp.SpringBootWork.system.mapper.TokenMapper;
@Service
@RequiredArgsConstructor
public class AuthServiceImp {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleMapper roleMapper;
    private final TokenMapper tokenMapper;

    @Transactional(rollbackFor = Exception.class)
    public void demoAddUser(RegistrationRequest request) {

        if (userMapper.findByEmailToUser(request.getEmail()) != null) {
            throw new BusinessException(ResultCode.USER_EXISTS);
        }

        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(1)
                .roleId(getDefaultRoleId())
                .build();

        userMapper.insert(newUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public Token login(LoginRequest request) {
        AuthUser user = userMapper.findByEmailToAuthUser(request.getEmail());
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.INVALID_CREDENTIALS);
        }
        String accessToken = jwtService.generateToken(user);
        Token token = Token.builder()
                .userId(user.getUserId())
                .token(accessToken)
                .tokenType(TokenType.ACCESS)
                .build();
        tokenMapper.insert(token);
        return token;
    }

    private Long getDefaultRoleId() {
        return roleMapper.findDefaultRoleId();
    }

    public String getPassword(Long userId) {
        return userMapper.findPasswordById(userId);
    }

}
