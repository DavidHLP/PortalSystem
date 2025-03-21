package com.david.hlp.Spring.repeater.controller;

import com.david.hlp.Spring.repeater.entity.RouterUrl;
import com.david.hlp.Spring.repeater.service.RouterUrlServiceImp;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.common.result.Result;

@RestController
@RequestMapping("/api/repeater/router-urls")
@RequiredArgsConstructor
public class RouterUrlController {
    
    private final RouterUrlServiceImp routerUrlService;
    
    @GetMapping
    public Result<PageInfo<RouterUrl>> findAll() {
        PageInfo<RouterUrl> pageInfo = new PageInfo<>();
        pageInfo.setItems(routerUrlService.findAll());
        return Result.success(pageInfo);
    }
    
    @GetMapping("/{id}")
    public Result<RouterUrl> findById(@PathVariable Integer id) {
        return Result.success(routerUrlService.findById(id));
    }
    
    @PostMapping
    public Result<RouterUrl> create(@RequestBody RouterUrl routerUrl) {
        return Result.success(routerUrlService.create(routerUrl));
    }
    
    @PutMapping("/{id}")
    public Result<RouterUrl> update(@PathVariable Integer id, @RequestBody RouterUrl routerUrl) {
        routerUrl.setId(id);
        return Result.success(routerUrlService.update(routerUrl));
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        routerUrlService.deleteById(id);
        return Result.success();
    }
}
