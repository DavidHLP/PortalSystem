package com.david.hlp.Spring.repeater.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.common.result.Result;
import com.david.hlp.Spring.repeater.entity.ProjectUrl;
import com.david.hlp.Spring.repeater.service.ProjectUrlService;
import com.david.hlp.Spring.common.enums.ResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import java.util.List;
/**
 * 项目URL控制器
 *
 * @author david
 * @version 1.0
 */
@Tag(name = "项目URL管理", description = "项目URL的增删改查接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/repeater/project-url")
public class ProjectUrlController {

    private final ProjectUrlService projectUrlService;

    /**
     * 分页查询项目URL列表
     *
     * @param pageInfo 分页查询条件
     * @return 分页查询结果
     */
    @Operation(summary = "分页查询项目URL列表", description = "根据条件分页查询项目URL信息")
    @PostMapping("/list")
    public Result<PageInfo<ProjectUrl>> list(@RequestBody(required = false) PageInfo<ProjectUrl> pageInfo) {
        if (pageInfo == null) {
            return Result.error(ResultCode.BAD_REQUEST);
        }
        return Result.success(projectUrlService.getPage(
            pageInfo.getPageNum(),
            pageInfo.getPageSize(),
            pageInfo.getQuery()));
    }

    /**
     * 获取项目URL详情
     *
     * @param id 项目URL ID
     * @return 项目URL详情
     */
    @Operation(summary = "获取项目URL详情", description = "根据ID获取项目URL详细信息")
    @PostMapping("/get")
    public Result<ProjectUrl> get(@RequestBody ProjectUrl projectUrl) {
        if (projectUrl == null || projectUrl.getId() == null) {
            return Result.error(ResultCode.BAD_REQUEST);
        }
        return Result.success(projectUrlService.getById(projectUrl.getId()));
    }

    /**
     * 新增项目URL
     *
     * @param projectUrl 项目URL信息
     * @return 新增结果
     */
    @Operation(summary = "新增项目URL", description = "新增项目URL信息")
    @PostMapping("/add")
    public Result<Void> add(@Validated @RequestBody ProjectUrl projectUrl) {
        if (projectUrl == null) {
            return Result.error(ResultCode.BAD_REQUEST);
        }
        projectUrlService.create(projectUrl);
        return Result.success();
    }

    /**
     * 更新项目URL
     *
     * @param projectUrl 项目URL信息
     * @return 更新结果
     */
    @Operation(summary = "更新项目URL", description = "更新项目URL信息")
    @PostMapping("/update")
    public Result<Void> update(@Validated @RequestBody ProjectUrl projectUrl) {
        if (projectUrl == null || projectUrl.getId() == null) {
            return Result.error(ResultCode.BAD_REQUEST);
        }
        projectUrlService.update(projectUrl);
        return Result.success();
    }

    /**
     * 删除项目URL
     *
     * @param projectUrl 项目URL信息
     * @return 删除结果
     */
    @Operation(summary = "删除项目URL", description = "根据ID删除项目URL信息")
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody ProjectUrl projectUrl) {
        if (projectUrl == null || projectUrl.getId() == null) {
            return Result.error(ResultCode.BAD_REQUEST);
        }
        projectUrlService.deleteById(projectUrl.getId());
        return Result.success();
    }

    /**
     * 获取所有项目URL
     *
     * @return 所有项目URL列表
     */
    @Operation(summary = "获取所有项目URL", description = "获取所有项目URL列表")
    @PostMapping("/listAll")
    public Result<List<ProjectUrl>> listAll() {
        return Result.success(projectUrlService.listAll());
    }
}
