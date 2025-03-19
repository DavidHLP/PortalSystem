package com.david.hlp.SpringBootWork.repeater.controller;

import org.springframework.web.bind.annotation.RestController;

import com.david.hlp.SpringBootWork.repeater.service.ProjectUrlServiceImp;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

import com.david.hlp.SpringBootWork.common.result.PageInfo;
import com.david.hlp.SpringBootWork.common.result.Result;
import com.david.hlp.SpringBootWork.repeater.entity.ProjectUrl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/repeater/project-url")
public class ProjectUrlController {
    private final ProjectUrlServiceImp projectUrlService;

    @PostMapping("/findAll")
    public Result <PageInfo<ProjectUrl>> findAll(@RequestBody(required = false) PageInfo<ProjectUrl> pageInfo) {
        return Result.success(projectUrlService.findAll(
            pageInfo.getPageSize(),
            pageInfo.getPageNum(),
            pageInfo.getQuery().getProjectName(),
            pageInfo.getQuery().getProjectInterfaceName()));
    }

    @PostMapping("/delete")
    public Result <Void> delete(@RequestBody ProjectUrl projectUrl) {
        projectUrlService.delete(projectUrl);
        return Result.success("删除成功");
    }

    @PostMapping("/update")
    public Result <Void> update(@RequestBody ProjectUrl projectUrl) {
        projectUrlService.update(projectUrl);
        return Result.success("更新成功");
    }
}
