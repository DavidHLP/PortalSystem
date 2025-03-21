package com.david.hlp.Spring.repeater.controller;

import com.david.hlp.Spring.common.result.Result;
import com.david.hlp.Spring.repeater.entity.Url;
import com.david.hlp.Spring.repeater.entity.HostUrl;
import com.david.hlp.Spring.repeater.entity.PortUrl;
import com.david.hlp.Spring.repeater.entity.RouterUrl;
import com.david.hlp.Spring.repeater.entity.ProjectUrl;
import com.david.hlp.Spring.repeater.service.PathUrlServiceImp;
import com.david.hlp.Spring.repeater.service.HostServiceImp;
import com.david.hlp.Spring.repeater.service.PortServiceImp;
import com.david.hlp.Spring.repeater.service.RouterUrlServiceImp;
import com.david.hlp.Spring.repeater.service.ProjectUrlServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import com.david.hlp.Spring.repeater.entity.ProjectUrlRequest;
import com.david.hlp.Spring.common.result.PageInfo;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/repeater/path-urls")
public class PathUrlController {
    private final PathUrlServiceImp pathUrlService;
    private final HostServiceImp hostService;
    private final PortServiceImp portService;
    private final RouterUrlServiceImp routerUrlService;
    private final ProjectUrlServiceImp projectUrlService;

    @GetMapping("/{id}")
    public Result<Url> findById(@PathVariable Integer id) {
        return Result.success(pathUrlService.findById(id));
    }

    @GetMapping
    public Result<PageInfo<ProjectUrlRequest>> findAllProjectUrlRequest(
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer limit,
        @RequestParam(defaultValue = "") Integer routerId,
        @RequestParam(defaultValue = "") Integer hostId,
        @RequestParam(defaultValue = "") Integer portId,
        @RequestParam(defaultValue = "") Integer projectId,
        @RequestParam(defaultValue = "") String method,
        @RequestParam(defaultValue = "") Integer isActive,
        @RequestParam(defaultValue = "") String protocol
    ) {
        return Result.success(pathUrlService.findAllProjectUrlRequest(page, limit, routerId, hostId, portId, projectId, method, isActive, protocol));
    }

    @GetMapping("/listAllHost")
    public Result<List<HostUrl>> listAllHost() {
        return Result.success(hostService.listAll());
    }

    @GetMapping("/listAllPort")
    public Result<List<PortUrl>> listAllPort() {
        return Result.success(portService.listAll());
    }

    @GetMapping("/listAllRouter")
    public Result<List<RouterUrl>> listAllRouter() {
        return Result.success(routerUrlService.listAll());
    }

    @GetMapping("/listAllProject")
    public Result<List<ProjectUrl>> listAllProject() {
        return Result.success(projectUrlService.listAll());
    }

    @PostMapping
    public Result<Url> create(@RequestBody Url url) {
        return Result.success(pathUrlService.create(url));
    }

    @PutMapping("/{id}")
    public Result<Url> update(@PathVariable Integer id, @RequestBody Url url) {
        url.setId(id);
        return Result.success(pathUrlService.update(url));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        pathUrlService.deleteById(id);
        return Result.success();
    }
}