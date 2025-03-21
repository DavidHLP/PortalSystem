package com.david.hlp.Spring.repeater.service.impl;

import org.springframework.stereotype.Service;

import com.david.hlp.Spring.repeater.mapper.ProjectMapper;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.repeater.entity.ProjectUrl;
import com.david.hlp.Spring.repeater.service.ProjectUrlService;

import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * 项目URL服务实现类
 *
 * @author david
 * @date 2024/03/21
 */
@Service
@RequiredArgsConstructor
public class ProjectUrlServiceImpl implements ProjectUrlService {
    private final ProjectMapper projectMapper;

    /**
     * 创建项目URL
     *
     * @param projectUrl 项目URL信息
     * @return 创建后的项目URL信息
     */
    @Override
    public ProjectUrl create(ProjectUrl projectUrl) {
        projectMapper.insertProject(projectUrl);
        return projectUrl;
    }

    /**
     * 根据ID获取项目URL
     *
     * @param id 项目URL ID
     * @return 项目URL信息
     */
    @Override
    public ProjectUrl getById(Integer id) {
        return projectMapper.getProjectById(id);
    }

    /**
     * 获取所有项目URL
     *
     * @return 项目URL列表
     */
    @Override
    public PageInfo<ProjectUrl> listAll() {
        List<ProjectUrl> projectUrls = projectMapper.listProjectBasicInfo();
        return PageInfo.<ProjectUrl>builder()
            .items(projectUrls)
            .total((long)projectUrls.size())
            .build();
    }

    /**
     * 根据ID删除项目URL
     *
     * @param id 项目URL ID
     */
    @Override
    public void deleteById(Integer id) {
        projectMapper.deleteProjectById(id);
    }

    /**
     * 更新项目URL
     *
     * @param projectUrl 项目URL信息
     * @return 更新后的项目URL信息
     */
    @Override
    public ProjectUrl update(ProjectUrl projectUrl) {
        projectMapper.updateProject(projectUrl);
        return projectUrl;
    }

    /**
     * 分页获取项目URL列表
     *
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<ProjectUrl> getPage(Integer pageNum, Integer pageSize) {
        Integer offset = (pageNum - 1) * pageSize;
        List<ProjectUrl> projectUrls = projectMapper.listProjects(pageSize, offset, null, null);
        Long total = projectMapper.getProjectCount(null, null);
        return PageInfo.<ProjectUrl>builder()
            .items(projectUrls)
            .pageNum(pageNum)
            .pageSize(pageSize)
            .total(total)
            .build();
    }

    /**
     * 分页查询项目URL列表
     *
     * @param pageSize 每页大小
     * @param pageNum 页码
     * @param projectName 项目名称
     * @param projectInterfaceName 接口名称
     * @return 分页结果
     */
    @Override
    public PageInfo<ProjectUrl> getProjectUrlList(Integer pageSize, Integer pageNum, String projectName, String projectInterfaceName) {
        Integer offset = (pageNum - 1) * pageSize;
        List<ProjectUrl> projectUrls = projectMapper.listProjects(pageSize, offset, projectName, projectInterfaceName);
        Long total = projectMapper.getProjectCount(projectName, projectInterfaceName);
        return PageInfo.<ProjectUrl>builder()
            .items(projectUrls)
            .pageNum(pageNum)
            .pageSize(pageSize)
            .total(total)
            .build();
    }

    /**
     * 删除项目URL
     *
     * @param projectUrl 项目URL信息
     */
    @Override
    public void removeProjectUrl(ProjectUrl projectUrl) {
        projectMapper.deleteProjectById(projectUrl.getId());
    }

    /**
     * 更新项目URL
     *
     * @param projectUrl 项目URL信息
     */
    @Override
    public void updateProjectUrl(ProjectUrl projectUrl) {
        projectMapper.updateProject(projectUrl);
    }

    /**
     * 获取所有项目URL列表
     *
     * @return 项目URL列表
     */
    @Override
    public List<ProjectUrl> getProjectUrlList() {
        return projectMapper.listProjectBasicInfo();
    }
}