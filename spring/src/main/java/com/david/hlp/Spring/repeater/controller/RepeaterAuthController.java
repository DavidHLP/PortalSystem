package com.david.hlp.Spring.repeater.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.david.hlp.Spring.repeater.entity.TokenUrl;
import com.david.hlp.Spring.repeater.entity.UserUrl;
import com.david.hlp.Spring.system.service.AuthService;
import com.david.hlp.Spring.system.entity.auth.LoginDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import com.david.hlp.Spring.common.result.Result;
import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/repeater/auth")
public class RepeaterAuthController {

    @Qualifier("repeaterAuthServiceImp")
    private final AuthService<LoginDTO,UserUrl,TokenUrl> authService;

    @PostMapping("/login")
    public Result<TokenUrl> login(@RequestBody LoginDTO loginDTO) {
        return Result.success(authService.login(loginDTO));
    }
}
