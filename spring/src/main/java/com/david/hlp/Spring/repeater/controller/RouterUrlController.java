package com.david.hlp.Spring.repeater.controller;

import com.david.hlp.Spring.repeater.entity.RouterUrl;
import com.david.hlp.Spring.repeater.service.RouterUrlService;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 路由URL控制器
 *
 * @author david
 */
@Tag(name = "路由URL管理", description = "路由URL的CRUD操作")
@RestController
@RequestMapping("/api/repeater/router-urls")
@RequiredArgsConstructor
@Validated
public class RouterUrlController {

    private final RouterUrlService routerUrlService;

    @Operation(summary = "获取路由URL列表", description = "分页获取路由URL信息，支持条件查询")
    @ApiResponse(responseCode = "200", description = "成功获取路由URL列表",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = PageInfo.class)))
    @GetMapping
    public Result<PageInfo<RouterUrl>> listRouterUrls(
            @Parameter(description = "页码", required = true)
            @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数", required = true)
            @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "路径")
            @RequestParam(required = false) String path) {
        RouterUrl query = RouterUrl.builder().path(path).build();
        return Result.success(routerUrlService.getPage(pageNum, pageSize, query));
    }

    @Operation(summary = "根据ID获取路由URL", description = "通过ID查询单个路由URL信息")
    @ApiResponse(responseCode = "200", description = "成功获取路由URL信息")
    @GetMapping("/{id}")
    public Result<RouterUrl> getRouterUrlById(
            @Parameter(description = "路由URL ID", required = true)
            @PathVariable Integer id) {
        return Result.success(routerUrlService.getById(id));
    }

    @Operation(summary = "创建路由URL", description = "创建新的路由URL")
    @ApiResponse(responseCode = "200", description = "成功创建路由URL")
    @PostMapping
    public Result<RouterUrl> createRouterUrl(
            @Parameter(description = "路由URL信息", required = true)
            @Valid @RequestBody RouterUrl routerUrl) {
        return Result.success(routerUrlService.create(routerUrl));
    }

    @Operation(summary = "更新路由URL", description = "根据ID更新路由URL信息")
    @ApiResponse(responseCode = "200", description = "成功更新路由URL")
    @PutMapping("/{id}")
    public Result<RouterUrl> updateRouterUrl(
            @Parameter(description = "路由URL ID", required = true)
            @PathVariable Integer id,
            @Parameter(description = "路由URL信息", required = true)
            @Valid @RequestBody RouterUrl routerUrl) {
        routerUrl.setId(id);
        return Result.success(routerUrlService.update(routerUrl));
    }

    @Operation(summary = "删除路由URL", description = "根据ID删除路由URL")
    @ApiResponse(responseCode = "200", description = "成功删除路由URL")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRouterUrl(
            @Parameter(description = "路由URL ID", required = true)
            @PathVariable Integer id) {
        routerUrlService.deleteById(id);
        return Result.success();
    }
}
