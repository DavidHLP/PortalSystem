package com.david.hlp.SpringBootWork.system.controller;

import com.david.hlp.SpringBootWork.system.entity.user.User;
import com.david.hlp.SpringBootWork.system.service.UserServiceImp;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.david.hlp.SpringBootWork.common.enums.ResultCode;
import com.david.hlp.SpringBootWork.common.exception.ApiException;
import com.david.hlp.SpringBootWork.common.result.PageInfo;
import com.david.hlp.SpringBootWork.common.result.Result;
import com.david.hlp.SpringBootWork.system.entity.user.DelUser;
import com.david.hlp.SpringBootWork.system.service.PasswordServiceImp;
import com.david.hlp.SpringBootWork.system.service.AuthServiceImp;
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController extends BaseController{
    private final UserServiceImp userService;
    private final PasswordServiceImp passwordService;
    private final AuthServiceImp authService;
    @PostMapping("/getUserManageInfo")
    public Result<PageInfo<User>> getUserManageInfo(@RequestBody PageInfo<User> pageInfo) {
        return Result.success(userService.getUserManageInfo(
                pageInfo.getPageNum(),
                pageInfo.getPageSize(),
                pageInfo.getQuery()
            ));
    }
    @PostMapping("/deleteUser")
    public Result<Void> deleteUser(@RequestBody DelUser user) {
        if (!passwordService.matches(user.getPassword(), authService.getPassword(getCurrentUserId()))) {
            throw new ApiException(ResultCode.PASSWORD_ERROR);
        }
        userService.deleteUser(user);
        return Result.success("删除成功");
    }

    @PostMapping("/updateUser")
    public Result<Void> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return Result.success("更新成功");
    }
}