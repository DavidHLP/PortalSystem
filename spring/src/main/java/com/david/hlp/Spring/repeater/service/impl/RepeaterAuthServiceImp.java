package com.david.hlp.Spring.repeater.service.impl;

import com.david.hlp.Spring.repeater.mapper.UserUrlMapper;
import com.david.hlp.Spring.system.entity.auth.LoginDTO;
import com.david.hlp.Spring.system.entity.auth.RegistrationDTO;
import com.david.hlp.Spring.system.service.AuthService;
import com.david.hlp.Spring.system.token.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.david.hlp.Spring.system.mapper.TokenMapper;
import com.david.hlp.Spring.system.auth.JwtService;
import com.david.hlp.Spring.system.mapper.RoleMapper;
import com.david.hlp.Spring.common.exception.BusinessException;
import com.david.hlp.Spring.common.enums.ResultCode;
import com.david.hlp.Spring.repeater.entity.TokenUrl;
import com.david.hlp.Spring.repeater.entity.UserUrl;
import com.david.hlp.Spring.system.token.TokenType;
import com.david.hlp.Spring.repeater.mapper.TokenUrlMapper;

@Service
@RequiredArgsConstructor
public class RepeaterAuthServiceImp implements AuthService<LoginDTO,UserUrl,TokenUrl> {

    private final UserUrlMapper userUrlMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenUrlMapper tokenUrlMapper;

    @Override
    public void addUser(UserUrl userUrl) {
        if (userUrlMapper.getByEmail(userUrl.getEmail()) != null) {
            throw new BusinessException(ResultCode.USER_EXISTS);
        }

        userUrl.setPasswordHash(passwordEncoder.encode(userUrl.getPasswordHash()));
        userUrlMapper.insert(userUrl);
    }

    @Override
    public TokenUrl login(LoginDTO loginDTO) {
        UserUrl userUrl = userUrlMapper.getByEmail(loginDTO.getEmail());
        if (userUrl == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(passwordEncoder.encode(loginDTO.getPassword()), userUrl.getPasswordHash())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }
        String accessToken = jwtService.generateToken(userUrl);
        TokenUrl tokenUrl = TokenUrl.builder()
                .userId((long)userUrl.getId())
                .token(accessToken)
                .tokenType(TokenType.ACCESS)
                .build();

        tokenUrlMapper.insert(tokenUrl);
        return tokenUrl;
    }

    @Override
    public Long getDefaultRoleId() {
        return null;
    }

    @Override
    public String getPassword(Long userId) {
        return null;
    }

}
