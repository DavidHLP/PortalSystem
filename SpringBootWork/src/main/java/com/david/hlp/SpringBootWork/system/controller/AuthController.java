package com.david.hlp.SpringBootWork.system.controller;

import com.david.hlp.SpringBootWork.system.entity.auth.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.david.hlp.SpringBootWork.system.service.AuthServiceImp;
import com.david.hlp.SpringBootWork.system.entity.role.Role;

import java.util.List;
import org.springframework.dao.DuplicateKeyException;

import com.david.hlp.SpringBootWork.common.enums.ResultCode;
import com.david.hlp.SpringBootWork.common.exception.BusinessException;
import com.david.hlp.SpringBootWork.common.result.Result;
import com.david.hlp.SpringBootWork.system.entity.auth.LoginRequest;
import com.david.hlp.SpringBootWork.system.token.Token;
import com.david.hlp.SpringBootWork.system.entity.router.Router;
import com.david.hlp.SpringBootWork.system.service.RouterServiceImp;
import com.david.hlp.SpringBootWork.system.service.PermissionServiceImp;
import com.david.hlp.SpringBootWork.system.service.RoleServiceImp;
import com.david.hlp.SpringBootWork.system.service.UserServiceImp;
import com.david.hlp.SpringBootWork.system.entity.user.User;
import com.david.hlp.SpringBootWork.system.entity.role.RolePermissionUpdateResponse;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController{

    private final AuthServiceImp authService;
    private final RouterServiceImp routerService;
    private final PermissionServiceImp permissionService;
    private final RoleServiceImp roleService;
    private final UserServiceImp userService;
    @PostMapping("/demo/register")
    public Result<String> registerUser( @RequestBody RegistrationRequest request) {
        try {
            authService.demoAddUser(request);
            return Result.success("注册成功");
        } catch (DuplicateKeyException e) {
            throw new BusinessException(ResultCode.USER_EXISTS);
        } catch (Exception e) {
            return Result.error(ResultCode.INTERNAL_ERROR, "注册失败: " + e.getMessage());
        }
    }

    @PostMapping("/demo/login")
    public Result<Token> login(@RequestBody LoginRequest request) {
        try {
            if (request.getEmail() == null || request.getPassword() == null) {
                throw new BusinessException(ResultCode.BAD_REQUEST);
            }
            Token token = authService.login(request);
            return Result.success(token);
        } catch (Exception e) {
            return Result.error(ResultCode.INTERNAL_ERROR, "登录失败: " + e.getMessage());
        }
    }

    @GetMapping("/demo/getRouters")
    public Result<List<Router>> getRouters() {
        List<Router> routers = routerService.getRouters();
        return Result.success(routers);
    }
    @GetMapping("/demo/getUserPrivateInformation")
    public Result<List<String>> getUserPrivateInformationByUserId() {
        List<String> permission = permissionService.getUserPrivateInformationByUserId(getCurrentUserId());
        return Result.success(permission);
    }

    @GetMapping("/demo/getUserRole")
    public Result<Role> getUserRole() {
        Long roleId = userService.getUserBaseInfo(getCurrentUserId()).getRoleId();
        Role role = roleService.getRole(roleId);
        return Result.success(role);
    }

    @GetMapping("/demo/getUserBaseInfo")
    public Result<User> getUserBaseInfo() {
        User user = userService.getUserBaseInfo(getCurrentUserId());
        return Result.success(user);
    }

    @PostMapping("/demo/editRouter")
    public Result<Void> editRouter(@RequestBody Router router) {
        routerService.editRouter(router);
        return Result.success("编辑成功");
    }

    @PostMapping("/demo/addRouter")
    public Result<Void> addRouter(@RequestBody Router router) {
        routerService.addRouter(router);
        return Result.success("添加成功");
    }

    @PostMapping("/demo/deleteRouter")
    public Result<Void> deleteRouter(@RequestBody Router router) {
        routerService.deleteRouter(router);
        return Result.success("删除成功");
    }

    @GetMapping("/demo/getRoleList")
    public Result<List<Role>> getRoleList() {
        List<Role> roleList = roleService.getRoleList();
        return Result.success(roleList);
    }

    @PostMapping("/demo/addRole")
    public Result<Void> addRole(@RequestBody Role role) {
        roleService.addRole(role);
        return Result.success("添加成功");
    }

    @PostMapping("/demo/editRole")
    public Result<Void> editRole(@RequestBody Role role) {
        roleService.editRole(role);
        return Result.success("编辑成功");
    }

    @PostMapping("/demo/updateRoleRouters")
    public Result<Void> updateRolePermissions(@RequestBody RolePermissionUpdateResponse rolePermissionUpdateResponse) {
        roleService.updateRolePermissions(rolePermissionUpdateResponse);
        return Result.success("更新成功");
    }

    @PostMapping("/demo/deleteRole")
    public Result<Void> deleteRole(@RequestBody Role role) {
        roleService.deleteRole(role.getId());
        return Result.success("删除成功");
    }
}