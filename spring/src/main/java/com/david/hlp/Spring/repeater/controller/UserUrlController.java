package com.david.hlp.Spring.repeater.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.david.hlp.Spring.repeater.service.impl.UserUrlServiceImpl;
import com.david.hlp.Spring.repeater.module.entity.UserRoleProject;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.common.result.Result;
import com.david.hlp.Spring.repeater.module.dto.UserRoleProjectDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/userUrl")
@RequiredArgsConstructor
public class UserUrlController {
    private final UserUrlServiceImpl userUrlService;

    /**
     * 分页查询用户角色项目关联信息
     *
     * @param pageInfo 分页查询条件
     * @return 分页结果
     */
    @PostMapping("/listUserRoleProjectByPage")
    public Result<PageInfo<UserRoleProject>> listUserRoleProjectByPage(@RequestBody PageInfo<UserRoleProject> pageInfo) {
        return Result.success(userUrlService.listUserRoleProjectByPage(pageInfo));
    }

    /**
     * 添加用户角色项目关联信息
     *
     * @param userRoleProject 用户角色项目关联信息
     * @return 添加结果
     */
    @PostMapping("/addUserRoleProject")
    public Result<Void> addUserRoleProject(@RequestBody UserRoleProjectDTO userRoleProjectDTO) {
        userUrlService.addUserRoleProject(userRoleProjectDTO);
        return Result.success();
    }


    /**
     * 删除用户角色项目关联信息
     *
     * @param userRoleProject 用户角色项目关联信息
     * @return 删除结果
     */
    @PostMapping("/deleteUserRoleProject")
    public Result<Void> deleteUserRoleProject(@RequestBody UserRoleProject userRoleProjectDTO) {
        userUrlService.deleteUserRoleProject(userRoleProjectDTO);
        return Result.success();
    }

    /**
     * 更新用户角色项目关联信息
     *
     * @param userRoleProject 用户角色项目关联信息
     * @return 更新结果
     */
    @PostMapping("/updateUserRoleProject")
    public Result<Void> updateUserRoleProject(@RequestBody UserRoleProject userRoleProjectDTO) {
        userUrlService.updateUserRoleProject(userRoleProjectDTO);
        return Result.success();
    }

    /**
     * 更新用户密码
     *
     * @param userRoleProject 用户角色项目关联信息
     * @return 更新结果
     */
    @PostMapping("/updateUserPassword")
    public Result<Void> updateUserPassword(@RequestBody UserRoleProjectDTO userRoleProjectDTO) {
        userUrlService.updateUserPassword(userRoleProjectDTO);
        return Result.success();
    }


}