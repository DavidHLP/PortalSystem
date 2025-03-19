package com.david.hlp.SpringBootWork.system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

import com.david.hlp.SpringBootWork.common.result.Result;
import com.david.hlp.SpringBootWork.system.entity.role.Role;
import com.david.hlp.SpringBootWork.system.service.RoleServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import com.david.hlp.SpringBootWork.system.util.annotation.validatestring.ValidateString;
@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleServiceImp roleService;

    @GetMapping("/getRoleList")
    public Result<List<Role>> getRoleList(@RequestParam(required = false) @ValidateString String roleName) {
        return Result.success(roleService.getRoleList(roleName));
    }
}
