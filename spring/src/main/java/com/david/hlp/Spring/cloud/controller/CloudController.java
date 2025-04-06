package com.david.hlp.Spring.cloud.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.david.hlp.Spring.cloud.module.entity.LoginRequest;
import com.david.hlp.Spring.cloud.module.entity.CloudToken;
import com.david.hlp.Spring.cloud.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import com.david.hlp.Spring.cloud.module.dto.CloudDTO;
import java.util.HashMap;
import com.david.hlp.Spring.cloud.module.dto.CloudData;
import com.david.hlp.Spring.common.result.Result;
import com.david.hlp.Spring.cloud.service.impl.CloudServiceImpl;
import com.david.hlp.Spring.cloud.module.dto.CloudRequest;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cloud")
public class CloudController extends CloudBaseController {
    private final AuthServiceImpl authService;
    private final CloudServiceImpl cloudService;

    @PostMapping("/login")
    public CloudToken login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/logout")
    public void logout(@RequestBody CloudToken cloudToken) {
    }

    @PostMapping("/post")
    public Result<HashMap<String, Object>> post(@RequestBody CloudRequest cloudRequest) {
        HashMap<String, Object> result = cloudService.post(cloudRequest, getCurrentUserEmail());
        return Result.success(result);
    }
}
