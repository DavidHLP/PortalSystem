package com.david.hlp.Spring.repeater.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.common.result.Result;
import com.david.hlp.Spring.repeater.entity.PortUrl;
import com.david.hlp.Spring.repeater.service.PortService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

import lombok.RequiredArgsConstructor;

/**
 * 端口URL管理控制器
 *
 * @author david
 * @version 1.0
 * @since 2024-01-01
 */
@Tag(name = "端口URL管理", description = "端口URL的增删改查接口")
@RestController
@RequestMapping("/api/repeater/port-url")
@RequiredArgsConstructor
public class PortController {

    /**
     * 端口服务接口
     */
    private final PortService portService;

    /**
     * 获取端口列表
     *
     * @param number 端口号（可选）
     * @param page   页码，默认值为1
     * @param limit  每页数量，默认值为10
     * @return Result<PageInfo<PortUrl>> 包含端口列表和总数的结果对象
     */
    @Operation(summary = "获取端口列表", description = "可根据端口号筛选，支持分页查询")
    @ApiResponse(responseCode = "200", description = "查询成功",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Result.class)))
    @GetMapping("/list")
    public Result<PageInfo<PortUrl>> getPortList(
            @Parameter(description = "端口号") @RequestParam(required = false) String number,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer limit) {
        PageInfo<PortUrl> data = portService.getPortList(limit, page, number);
        return Result.success(data);
    }

    /**
     * 获取端口详情
     *
     * @param id 端口ID
     * @return Result<PortUrl> 包含端口详情的结果对象
     */
    @Operation(summary = "获取端口详情", description = "根据ID获取端口详细信息")
    @ApiResponse(responseCode = "200", description = "查询成功",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = PortUrl.class)))
    @GetMapping("/{id}")
    public Result<PortUrl> getPortById(
            @Parameter(description = "端口ID", required = true)
            @PathVariable Long id) {
        Objects.requireNonNull(id, "id不能为空");
        PortUrl port = portService.getPortById(id);
        return Result.success(port);
    }

    /**
     * 创建端口
     *
     * @param port 端口信息
     * @return Result<PortUrl> 包含创建后的端口信息的结果对象
     */
    @Operation(summary = "创建端口", description = "创建新的端口URL配置")
    @ApiResponse(responseCode = "200", description = "创建成功",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = PortUrl.class)))
    @PostMapping
    public Result<PortUrl> createPort(
            @Parameter(description = "端口信息", required = true)
            @RequestBody PortUrl port) {
        Objects.requireNonNull(port, "port不能为空");
        PortUrl createdPort = portService.createPort(port);
        return Result.success(createdPort);
    }

    /**
     * 更新端口
     *
     * @param id   端口ID
     * @param port 端口信息
     * @return Result<Void> 更新结果
     */
    @Operation(summary = "更新端口", description = "根据ID更新端口URL配置")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PutMapping("/{id}")
    public Result<Void> updatePort(
            @Parameter(description = "端口ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "端口信息", required = true)
            @RequestBody PortUrl port) {
        Objects.requireNonNull(id, "id不能为空");
        Objects.requireNonNull(port, "port不能为空");
        portService.updatePort(id, port);
        return Result.success();
    }

    /**
     * 删除端口
     *
     * @param id 端口ID
     * @return Result<Void> 删除结果
     */
    @Operation(summary = "删除端口", description = "根据ID删除端口URL配置")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @DeleteMapping("/{id}")
    public Result<Void> deletePort(
            @Parameter(description = "端口ID", required = true)
            @PathVariable Long id) {
        Objects.requireNonNull(id, "id不能为空");
        portService.deletePort(id);
        return Result.success();
    }
}
