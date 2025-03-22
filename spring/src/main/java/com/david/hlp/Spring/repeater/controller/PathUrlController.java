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
import com.david.hlp.Spring.repeater.entity.ProjectUrlRequest;
import com.david.hlp.Spring.common.result.PageInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.Assert;

import java.util.List;

/**
 * URL路径控制器
 *
 * @author david
 * @date 2024/03/21
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/repeater/path-urls")
public class PathUrlController {
    private final PathUrlService pathUrlService;
    private final HostService hostService;
    private final PortService portService;
    private final RouterUrlService routerUrlService;
    private final ProjectUrlService projectUrlService;

    /**
     * 根据ID获取URL信息
     *
     * @param id URL ID
     * @return URL信息
     */
    @GetMapping("/{id}")
    public Result<Url> getById(@PathVariable Integer id) {
        Assert.notNull(id, "URL ID不能为空");
        return Result.success(pathUrlService.getById(id));
    }

    /**
     * 分页查询URL列表
     *
     * @param page 页码
     * @param limit 每页大小
     * @param routerId 路由ID
     * @param hostId 主机ID
     * @param portId 端口ID
     * @param projectId 项目ID
     * @param method 请求方法
     * @param isActive 是否激活
     * @param protocol 协议
     * @return 分页结果
     */
    @GetMapping
    public Result<PageInfo<ProjectUrlRequest>> listAllProjectUrlRequest(
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer limit,
        @RequestParam(required = false) Integer routerId,
        @RequestParam(required = false) Integer hostId,
        @RequestParam(required = false) Integer portId,
        @RequestParam(required = false) Integer projectId,
        @RequestParam(required = false) String method,
        @RequestParam(required = false) Integer isActive,
        @RequestParam(required = false) String protocol
    ) {
        Url url = Url.builder()
            .routerId(routerId)
            .hostId(hostId)
            .portId(portId)
            .projectId(projectId)
            .method(method)
            .isActive(isActive != null ? isActive == 1 : null)
            .protocol(protocol)
            .build();
        return Result.success(pathUrlService.getPageUltra(page, limit, url));
    }

    /**
     * 获取所有主机列表
     *
     * @return 主机列表
     */
    @GetMapping("/listAllHost")
    public Result<List<HostUrl>> listAllHost() {
        return Result.success(hostService.listAll());
    }

    /**
     * 获取所有端口列表
     *
     * @return 端口列表
     */
    @GetMapping("/listAllPort")
    public Result<List<PortUrl>> listAllPort() {
        return Result.success(portService.listAllPorts());
    }

    /**
     * 获取所有路由列表
     *
     * @return 路由列表
     */
    @GetMapping("/listAllRouter")
    public Result<List<RouterUrl>> listAllRouter() {
        return Result.success(routerUrlService.listRouterUrls());
    }

    /**
     * 获取所有项目列表
     *
     * @return 项目列表
     */
    @GetMapping("/listAllProject")
    public Result<List<ProjectUrl>> listAllProject() {
        return Result.success(projectUrlService.getProjectUrlList());
    }

    /**
     * 创建URL信息
     *
     * @param url URL信息
     * @return 创建后的URL信息
     */
    @PostMapping
    public Result<Url> create(@Validated @RequestBody Url url) {
        return Result.success(pathUrlService.create(url));
    }

    /**
     * 更新URL信息
     *
     * @param id URL ID
     * @param url URL信息
     * @return 更新后的URL信息
     */
    @PutMapping("/{id}")
    public Result<Url> update(@PathVariable Integer id, @Validated @RequestBody Url url) {
        Assert.notNull(id, "URL ID不能为空");
        url.setId(id);
        return Result.success(pathUrlService.update(url));
    }

    /**
     * 删除URL信息
     *
     * @param id URL ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        Assert.notNull(id, "URL ID不能为空");
        pathUrlService.deleteById(id);
        return Result.success();
    }
    /**
     * 获取所有URL列表
     *
     * @return 所有URL列表
     */
    @GetMapping("/listAll")
    public Result<List<Url>> listAll() {
        return Result.success(pathUrlService.listAll());
    }
}