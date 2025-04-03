package com.david.hlp.Spring.repeater.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.common.result.Result;
import com.david.hlp.Spring.repeater.module.entity.RoleUrl;
import com.david.hlp.Spring.repeater.service.impl.RoleUrlServiceImpl;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/roleUrl")
@RequiredArgsConstructor
public class RoleUrlController {
    private final RoleUrlServiceImpl roleUrlService;
    
    @PostMapping("/list")
    public Result<PageInfo<RoleUrl>> list(@RequestBody PageInfo<RoleUrl> pageInfo) {
        return Result.success(roleUrlService.listRoleUrlByPage(pageInfo));
    }
    
    @PostMapping("/add")
    public Result<Void> add(@RequestBody RoleUrl roleUrl) {
        roleUrlService.addRoleUrl(roleUrl);
        return Result.success("添加成功");
    }
    
    @PostMapping("/update")
    public Result<Void> update(@RequestBody RoleUrl roleUrl) {
        roleUrlService.updateRoleUrl(roleUrl);
        return Result.success("更新成功");
    }
    
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody RoleUrl roleUrl) {
        roleUrlService.deleteRoleUrl(roleUrl);
        return Result.success("删除成功");
    }

        /**
     * 获取角色列表
     *
     * @return 角色列表
     */
    @PostMapping("/getRoleList")
    public Result<List<RoleUrl>> getRoleList() {
        return Result.success(roleUrlService.getRoleList());
    }
}
