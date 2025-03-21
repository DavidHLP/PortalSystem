package com.david.hlp.Spring.repeater.controller;

import org.springframework.web.bind.annotation.*;

import com.david.hlp.Spring.repeater.service.HostServiceImp;
import com.david.hlp.Spring.common.enums.ResultCode;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.common.result.Result;
import com.david.hlp.Spring.repeater.entity.HostUrl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.RequiredArgsConstructor;

/**
 * 主机管理控制器
 * 提供主机的增删改查等REST接口
 *
 * @author david
 * @since 2024-01-01
 */
@Tag(name = "主机管理", description = "主机URL的增删改查接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/repeater/host-url")
public class HostController {
    private final HostServiceImp hostService;

    /**
     * 查询主机列表
     * @param limit 每页记录数
     * @param page 页码
     * @param address 地址筛选
     * @param isActive 状态筛选
     * @return 主机列表及总数
     */
    @Operation(summary = "获取主机列表", description = "分页查询主机列表，支持按地址和状态筛选")
    @ApiResponse(responseCode = "200", description = "查询成功", 
                content = @Content(schema = @Schema(implementation = PageInfo.class)))
    @GetMapping("/list")
    public Result<PageInfo<HostUrl>> findAll(
            @Parameter(description = "每页记录数") @RequestParam(required = false, defaultValue = "10") Integer limit,
            @Parameter(description = "页码") @RequestParam(required = false, defaultValue = "1") Integer page,
            @Parameter(description = "地址筛选") @RequestParam(required = false) String address,
            @Parameter(description = "状态筛选") @RequestParam(required = false) Boolean isActive) {

        Integer offset = (page - 1) * limit;
        PageInfo<HostUrl> data = hostService.findAllWithCount(limit, offset, address, isActive);
        return Result.success(data);
    }

    /**
     * 查询单个主机信息
     * @param id 主机ID
     * @return 主机信息
     */
    @Operation(summary = "获取单个主机", description = "根据ID查询主机详细信息")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @ApiResponse(responseCode = "404", description = "主机未找到")
    @GetMapping("/{id}")
    public Result<HostUrl> findById(@Parameter(description = "主机ID") @PathVariable Integer id) {
        HostUrl hostUrl = hostService.findById(id);
        if (hostUrl != null) {
            return Result.success(hostUrl);
        }
        return Result.error(ResultCode.NOT_FOUND, "主机未找到");
    }

    /**
     * 新增主机
     * @param hostUrl 主机信息
     * @return 处理结果
     */
    @Operation(summary = "创建主机", description = "创建新的主机记录")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @ApiResponse(responseCode = "400", description = "创建失败，地址已存在")
    @PostMapping
    public Result<HostUrl> create(@Parameter(description = "主机信息") @RequestBody HostUrl hostUrl) {
        if (hostService.create(hostUrl)) {
            return Result.success(hostUrl);
        }
        return Result.error(ResultCode.BAD_REQUEST, "添加失败，地址已存在");
    }

    /**
     * 更新主机信息
     * @param id 主机ID
     * @param hostUrl 主机信息
     * @return 处理结果
     */
    @Operation(summary = "更新主机", description = "更新指定ID的主机信息")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @ApiResponse(responseCode = "400", description = "更新失败，主机不存在或地址已被使用")
    @PutMapping("/{id}")
    public Result<String> update(
            @Parameter(description = "主机ID") @PathVariable Integer id,
            @Parameter(description = "主机信息") @RequestBody HostUrl hostUrl) {
        hostUrl.setId(id);
        if (hostService.update(hostUrl)) {
            return Result.success("更新成功");
        }
        return Result.error(ResultCode.BAD_REQUEST, "更新失败，主机不存在或地址已被使用");
    }

    /**
     * 删除主机
     * @param id 主机ID
     * @return 处理结果
     */
    @Operation(summary = "删除主机", description = "删除指定ID的主机")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @ApiResponse(responseCode = "404", description = "删除失败，主机不存在")
    @DeleteMapping("/{id}")
    public Result<String> delete(@Parameter(description = "主机ID") @PathVariable Integer id) {
        if (hostService.delete(id)) {
            return Result.success("删除成功");
        }
        return Result.error(ResultCode.NOT_FOUND, "删除失败，主机不存在");
    }

    /**
     * 更新主机状态
     * @param id 主机ID
     * @param isActive 状态
     * @return 处理结果
     */
    @Operation(summary = "更新主机状态", description = "更新指定ID主机的激活状态")
    @ApiResponse(responseCode = "200", description = "状态更新成功")
    @ApiResponse(responseCode = "404", description = "状态更新失败，主机不存在")
    @PatchMapping("/{id}/status")
    public Result<String> updateStatus(
            @Parameter(description = "主机ID") @PathVariable Integer id,
            @Parameter(description = "激活状态") @RequestParam Boolean isActive) {
        if (hostService.updateStatus(id, isActive)) {
            return Result.success("状态更新成功");
        }
        return Result.error(ResultCode.NOT_FOUND, "状态更新失败，主机不存在");
    }
}

