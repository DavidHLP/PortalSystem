package com.david.hlp.Spring.repeater.service.impl;

import org.springframework.stereotype.Service;

import com.david.hlp.Spring.repeater.mapper.ProjectMapper;
import com.david.hlp.Spring.repeater.module.entity.Project;
import com.david.hlp.Spring.common.result.PageInfo;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl {
    private final ProjectMapper projectMapper;

    public void addProject(Project project) {
        projectMapper.insertProject(project);
    }

    /**
     * 分页查询项目列表
     * @param pageInfo 分页查询条件
     * @return 分页结果
     */
    public PageInfo<Project> listProjectByPage(PageInfo<Project> pageInfo) {
        Integer pageNum = pageInfo.getPageNum();
        Integer pageSize = pageInfo.getPageSize();
        // 计算偏移量
        Integer offset = pageSize * (pageNum - 1);

        // 查询总数
        Long total = projectMapper.countProject(pageInfo.getItem());

        // 查询数据列表
        List<Project> projectList = projectMapper.listProject(pageInfo.getItem(), pageSize, offset);

        // 计算总页数
        Integer pages = (int)Math.ceil((double)total / pageSize);

        // 构建返回结果
        return PageInfo.<Project>builder()
            .items(projectList)
            .item(pageInfo.getItem())
            .pageNum(pageNum)
            .pageSize(pageSize)
            .total(total)
            .pages(pages)
            .build();
    }

    /**
     * 更新项目信息
     * @param project 项目信息
     */
    public void updateProject(Project project) {
        projectMapper.updateProject(project);
    }

    /**
     * 删除项目信息
     * @param project 项目信息
     */
    public void deleteProject(Project project) {
        projectMapper.deleteProject(project);
    }
}
