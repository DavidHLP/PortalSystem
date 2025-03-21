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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

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
     * 分页查询所有项目URL
     *
     * @param pageInfo 分页查询条件
     * @return 分页查询结果
     */
    @Operation(summary = "分页查询项目URL列表", description = "根据条件分页查询项目URL信息")
    @PostMapping("/findAll")
    public Result<PageInfo<ProjectUrl>> findAll(
            @Parameter(description = "分页查询参数")
            @RequestBody(required = false) PageInfo<ProjectUrl> pageInfo) {
        if (pageInfo == null) {
            return Result.error(ResultCode.BAD_REQUEST);
        }
        return Result.success(projectUrlService.getProjectUrlList(
            pageInfo.getPageSize(),
            pageInfo.getPageNum(),
            pageInfo.getQuery().getProjectName(),
            pageInfo.getQuery().getProjectInterfaceName()));
    }

    /**
     * 删除项目URL
     *
     * @param projectUrl 待删除的项目URL
     * @return 删除结果
     */
    @Operation(summary = "删除项目URL", description = "根据ID删除项目URL信息")
    @PostMapping("/delete")
    public Result<Void> delete(
            @Parameter(description = "项目URL对象")
            @RequestBody ProjectUrl projectUrl) {
        if (projectUrl == null || projectUrl.getId() == null) {
            return Result.error(ResultCode.BAD_REQUEST);
        }
        projectUrlService.removeProjectUrl(projectUrl);
        return Result.success("删除成功");
    }

    /**
     * 更新项目URL
     *
     * @param projectUrl 待更新的项目URL
     * @return 更新结果
     */
    @Operation(summary = "更新项目URL", description = "更新项目URL信息")
    @PostMapping("/update")
    public Result<Void> update(
            @Parameter(description = "项目URL对象")
            @RequestBody ProjectUrl projectUrl) {
        if (projectUrl == null || projectUrl.getId() == null) {
            return Result.error(ResultCode.BAD_REQUEST);
        }
        projectUrlService.updateProjectUrl(projectUrl);
        return Result.success("更新成功");
    }
}
