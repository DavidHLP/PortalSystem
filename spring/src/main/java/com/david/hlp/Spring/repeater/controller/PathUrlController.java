package com.david.hlp.Spring.repeater.controller;

import com.david.hlp.Spring.common.result.Result;
import com.david.hlp.Spring.repeater.entity.Url;
import com.david.hlp.Spring.repeater.service.HostService;
import com.david.hlp.Spring.repeater.service.PortService;
import com.david.hlp.Spring.repeater.service.ProjectUrlService;
import com.david.hlp.Spring.repeater.service.RouterUrlService;
import com.david.hlp.Spring.repeater.service.PathUrlService;
import com.david.hlp.Spring.repeater.entity.HostUrl;
import com.david.hlp.Spring.repeater.entity.PortUrl;
import com.david.hlp.Spring.repeater.entity.RouterUrl;
import com.david.hlp.Spring.repeater.entity.ProjectUrl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.david.hlp.Spring.repeater.entity.ProjectUrlRequest;
import com.david.hlp.Spring.common.result.PageInfo;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/repeater/path-urls")
public class PathUrlController {
    private final PathUrlService pathUrlService;
    private final HostService hostService;
    private final PortService portService;
    private final RouterUrlService routerUrlService;
    private final ProjectUrlService projectUrlService;

    @GetMapping("/{id}")
    public Result<Url> getById(@PathVariable Integer id) {
        return Result.success(pathUrlService.getById(id));
    }

    @GetMapping
    public Result<PageInfo<ProjectUrlRequest>> listAllProjectUrlRequest(
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
        return Result.success(pathUrlService.getProjectUrlRequestList(page, limit, routerId, hostId, portId, projectId, method, isActive, protocol));
    }

    @GetMapping("/listAllHost")
    public Result<PageInfo<HostUrl>> listAllHost() {
        return Result.success(hostService.getHostList(100, 0, null, null));
    }

    @GetMapping("/listAllPort")
    public Result<List<PortUrl>> listAllPort() {
        return Result.success(portService.listAllPorts());
    }

    @GetMapping("/listAllRouter")
    public Result<List<RouterUrl>> listAllRouter() {
        return Result.success(routerUrlService.listRouterUrls());
    }

    @GetMapping("/listAllProject")
    public Result<List<ProjectUrl>> listAllProject() {
        return Result.success(projectUrlService.getProjectUrlList());
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