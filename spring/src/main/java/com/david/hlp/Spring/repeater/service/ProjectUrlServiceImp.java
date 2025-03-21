package com.david.hlp.Spring.repeater.service;

import org.springframework.stereotype.Service;

import com.david.hlp.Spring.repeater.mapper.ProjectMapper;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.repeater.entity.ProjectUrl;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectUrlServiceImp {
    private final ProjectMapper projectMapper;

    public PageInfo<ProjectUrl> findAll(Integer pageSize, Integer pageNum, String projectName, String projectInterfaceName) {
        Integer offset = (pageNum - 1) * pageSize;
        Integer limit = pageSize * pageNum;

        List<ProjectUrl> projectUrls = projectMapper.findAll(limit, offset, projectName, projectInterfaceName);
        return PageInfo.<ProjectUrl>builder()
            .items(projectUrls)
            .pageNum(pageNum)
            .pageSize(pageSize)
            .total(projectMapper.countByLimitAndOffset(limit, offset, projectName, projectInterfaceName))
            .build();
    }

    public void delete(ProjectUrl projectUrl) {
        projectMapper.deleteById(projectUrl.getId());
    }

    public void update(ProjectUrl projectUrl) {
        projectMapper.updateById(projectUrl);
    }

    public List<ProjectUrl> listAll() {
        return projectMapper.listAll();
    }
}