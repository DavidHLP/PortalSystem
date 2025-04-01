package com.david.hlp.Spring.repeater.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import com.david.hlp.Spring.repeater.service.impl.RouterUrlServiceImpl;
import com.david.hlp.Spring.repeater.module.entity.RouterUrl;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.common.result.Result;
import java.util.List;

@RestController
@RequestMapping("/api/routerUrl")
@RequiredArgsConstructor
public class RouterUrlController {
    private final RouterUrlServiceImpl routerUrlService;
    
    /**
     * 添加路由信息
     * @param routerUrl 路由信息
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result<Void> addRouterUrl(@RequestBody RouterUrl routerUrl) {
        routerUrlService.addRouterUrl(routerUrl);
        return Result.success();
    }
    
    /**
     * 分页查询路由信息
     * @param pageInfo 分页查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public Result<PageInfo<RouterUrl>> listRouterUrlByPage(@RequestBody PageInfo<RouterUrl> pageInfo) {
        return Result.success(routerUrlService.listRouterUrlByPage(pageInfo));
    }
    
    /**
     * 更新路由信息
     * @param routerUrl 路由信息
     * @return 操作结果
     */
    @PostMapping("/update")
    public Result<Void> updateRouterUrl(@RequestBody RouterUrl routerUrl) {
        routerUrlService.updateRouterUrl(routerUrl);
        return Result.success();
    }
    
    /**
     * 删除路由信息
     * @param routerUrl 路由信息
     * @return 操作结果
     */
    @PostMapping("/delete")
    public Result<Void> deleteRouterUrl(@RequestBody RouterUrl routerUrl) {
        routerUrlService.deleteRouterUrl(routerUrl);
        return Result.success();
    }
    
    /**
     * 查询所有路由信息
     * @return 所有路由信息
     */
    @GetMapping("/list")
    public Result<List<RouterUrl>> listAllRouterUrl() {
        return Result.success(routerUrlService.listAllRouterUrl());
    }
}
