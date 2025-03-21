package com.david.hlp.Spring.repeater.controller;

import org.springframework.web.bind.annotation.*;

import com.david.hlp.Spring.repeater.service.HostServiceImp;
import com.david.hlp.Spring.common.enums.ResultCode;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.common.result.Result;
import com.david.hlp.Spring.repeater.entity.HostUrl;

import lombok.RequiredArgsConstructor;
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
    @GetMapping("/list")
    public Result<PageInfo<HostUrl>> findAll(
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Boolean isActive) {

        Integer offset = (page - 1) * limit;
        PageInfo<HostUrl> data = hostService.findAllWithCount(limit, offset, address, isActive);
        return Result.success(data);
    }

    /**
     * 查询单个主机信息
     * @param id 主机ID
     * @return 主机信息
     */
    @GetMapping("/{id}")
    public Result<HostUrl> findById(@PathVariable Integer id) {
        HostUrl hostUrl = hostService.findById(id);
        if (hostUrl != null) {
            return Result.success(hostUrl);
        } else {
            return Result.error(ResultCode.NOT_FOUND, "主机未找到");
        }
    }

    /**
     * 新增主机
     * @param hostUrl 主机信息
     * @return 处理结果
     */
    @PostMapping
    public Result<HostUrl> create(@RequestBody HostUrl hostUrl) {
        if (hostService.create(hostUrl)) {
            return Result.success(hostUrl);
        } else {
            return Result.error(ResultCode.BAD_REQUEST, "添加失败，地址已存在");
        }
    }

    /**
     * 更新主机信息
     * @param id 主机ID
     * @param hostUrl 主机信息
     * @return 处理结果
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Integer id, @RequestBody HostUrl hostUrl) {
        hostUrl.setId(id);
        if (hostService.update(hostUrl)) {
            return Result.success("更新成功");
        } else {
            return Result.error(ResultCode.BAD_REQUEST, "更新失败，主机不存在或地址已被使用");
        }
    }

    /**
     * 删除主机
     * @param id 主机ID
     * @return 处理结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        if (hostService.delete(id)) {
            return Result.success("删除成功");
        } else {
            return Result.error(ResultCode.NOT_FOUND, "删除失败，主机不存在");
        }
    }

    /**
     * 更新主机状态
     * @param id 主机ID
     * @param isActive 状态
     * @return 处理结果
     */
    @PatchMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Integer id, @RequestParam Boolean isActive) {
        if (hostService.updateStatus(id, isActive)) {
            return Result.success("状态更新成功");
        } else {
            return Result.error(ResultCode.NOT_FOUND, "状态更新失败，主机不存在");
        }
    }
}
