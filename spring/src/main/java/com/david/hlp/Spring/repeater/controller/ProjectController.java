package com.david.hlp.Spring.repeater.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.common.result.Result;
import com.david.hlp.Spring.repeater.module.entity.Project;
import com.david.hlp.Spring.repeater.service.impl.ProjectServiceImpl;
import com.david.hlp.Spring.repeater.module.dto.ProjectRoleDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectServiceImpl projectService;

    @PostMapping("/list")
    public Result<PageInfo<ProjectRoleDTO>> list(@RequestBody PageInfo<ProjectRoleDTO> pageInfo) {
        return Result.success(projectService.listProjectByPage(pageInfo));
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Project project) {
        projectService.addProject(project);
        return Result.success("添加成功");
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody Project project) {
        projectService.updateProject(project);
        return Result.success("更新成功");
    }

    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody Project project) {
        projectService.deleteProject(project);
        return Result.success("删除成功");
    }

    @GetMapping("/listAll")
    public Result<List<Project>> listAll() {
        return Result.success(projectService.listAllProject());
    }
}