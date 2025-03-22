package com.david.hlp.Spring.repeater.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.repeater.entity.RoleUrl;
import com.david.hlp.Spring.repeater.service.RoleUrlService;
import com.david.hlp.Spring.repeater.entity.Url;
import com.david.hlp.Spring.common.result.Result;
import java.util.List;
@RestController
@RequestMapping("/api/repeater/roleurl")
@RequiredArgsConstructor
public class RoleUrlController {
    private final RoleUrlService roleUrlService;

    @GetMapping("/page")
    public Result<PageInfo<RoleUrl>> getPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            RoleUrl entity) {
        return Result.success(roleUrlService.getPage(page, limit, entity));
    }

    @GetMapping("/{id}")
    public Result<RoleUrl> getById(@PathVariable Integer id) {
        return Result.success(roleUrlService.getById(id));
    }

    @PostMapping
    public Result<RoleUrl> create(@RequestBody RoleUrl entity) {
        return Result.success(roleUrlService.create(entity));
    }

    @PutMapping
    public Result<RoleUrl> update(@RequestBody RoleUrl entity) {
        return Result.success(roleUrlService.update(entity));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        roleUrlService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/getRoleUrlListByRoleId")
    public Result<List<Url>> getRoleUrlListByRoleId(@RequestParam Integer roleId) {
        return Result.success(roleUrlService.getRoleUrlListByRoleId(roleId));
    }

    @PostMapping("/batchAddUrls")
    public Result<Void> batchAddUrls(@RequestParam Integer roleId, @RequestBody List<Integer> urlIds) {
        roleUrlService.batchAddUrls(roleId, urlIds);
        return Result.success();
    }

    @DeleteMapping("/deleteRoleUrl")
    public Result<Void> deleteRoleUrl(@RequestParam Integer roleId, @RequestParam Integer urlId) {
        roleUrlService.deleteRoleUrl(roleId, urlId);
        return Result.success();
    }
}
