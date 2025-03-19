package com.david.hlp.SpringBootWork.repeater.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.david.hlp.SpringBootWork.common.result.Result;
import com.david.hlp.SpringBootWork.repeater.entity.PortUrl;
import com.david.hlp.SpringBootWork.repeater.service.PortServiceImp;

import java.util.Map;

import lombok.RequiredArgsConstructor;

/**
 * 端口URL管理控制器
 */
@RestController
@RequestMapping("/api/repeater/port-url")
@RequiredArgsConstructor
public class PortController {
    
    private final PortServiceImp portService;
    
    /**
     * 获取端口列表
     * @param number 端口号（可选）
     * @param page 页码
     * @param limit 每页数量
     * @return 端口列表和总数
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getPortList(
            @RequestParam(required = false) String number,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Map<String, Object> data = portService.getPortList(number, page, limit);
        return Result.success(data);
    }
    
    /**
     * 获取端口详情
     * @param id 端口ID
     * @return 端口详情
     */
    @GetMapping("/{id}")
    public Result<PortUrl> getPortById(@PathVariable Integer id) {
        PortUrl port = portService.getPortById(id);
        return Result.success(port);
    }
    
    /**
     * 创建端口
     * @param port 端口信息
     * @return 创建后的端口信息
     */
    @PostMapping
    public Result<PortUrl> createPort(@RequestBody PortUrl port) {
        PortUrl createdPort = portService.createPort(port);
        return Result.success(createdPort);
    }
    
    /**
     * 更新端口
     * @param id 端口ID
     * @param port 端口信息
     * @return 成功消息
     */
    @PutMapping("/{id}")
    public Result<Void> updatePort(@PathVariable Integer id, @RequestBody PortUrl port) {
        portService.updatePort(id, port);
        return Result.success();
    }
    
    /**
     * 删除端口
     * @param id 端口ID
     * @return 成功消息
     */
    @DeleteMapping("/{id}")
    public Result<Void> deletePort(@PathVariable Integer id) {
        portService.deletePort(id);
        return Result.success();
    }
}
