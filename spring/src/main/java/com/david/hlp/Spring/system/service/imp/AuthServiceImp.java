package com.david.hlp.Spring.system.service.imp;

import org.springframework.stereotype.Service;
import com.david.hlp.Spring.system.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.david.hlp.Spring.common.enums.ResultCode;
import com.david.hlp.Spring.common.enums.TokenType;
import com.david.hlp.Spring.system.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import com.david.hlp.Spring.system.entity.user.User;
import com.david.hlp.Spring.system.entity.auth.RegistrationDTO;
import org.springframework.transaction.annotation.Transactional;
import com.david.hlp.Spring.system.token.Token;
import com.david.hlp.Spring.system.entity.auth.LoginDTO;
import com.david.hlp.Spring.common.exception.BusinessException;
import com.david.hlp.Spring.common.service.JwtCommonService;
import com.david.hlp.Spring.system.entity.auth.AuthUser;
import com.david.hlp.Spring.system.mapper.TokenMapper;
import com.david.hlp.Spring.system.service.AuthService;
/**
 * 认证服务实现类
 *
 * @author david
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService<LoginDTO, RegistrationDTO , Token> {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtCommonService<AuthUser> jwtService;
    private final RoleMapper roleMapper;
    private final TokenMapper tokenMapper;

    /**
     * 演示用户注册
     *
     * @param request 注册请求对象
     * @throws BusinessException 当用户已存在时抛出异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(RegistrationDTO request) {
        if (userMapper.getByEmailToUser(request.getEmail()) != null) {
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

    /**
     * 用户登录
     *
     * @param request 登录请求对象
     * @return Token 登录成功后的令牌
     * @throws BusinessException 当用户不存在或密码错误时抛出异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Token login(LoginDTO request) {
        AuthUser user = userMapper.getByEmailToAuthUser(request.getEmail());
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

    /**
     * 获取默认角色ID
     *
     * @return 默认角色ID
     */
    @Override
    public Long getDefaultRoleId() {
        return roleMapper.getDefaultRoleId();
    }

    /**
     * 根据用户ID获取密码
     *
     * @param userId 用户ID
     * @return 用户密码
     */
    @Override
    public String getPassword(Long userId) {
        return userMapper.getPasswordById(userId);
    }

}
