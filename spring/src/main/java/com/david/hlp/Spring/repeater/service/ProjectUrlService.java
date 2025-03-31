package com.david.hlp.Spring.repeater.service;

import com.david.hlp.Spring.common.interfaceI.BaseService;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.repeater.entity.ProjectUrl;

import java.util.List;

/**
 * 项目URL服务接口
 */
public interface ProjectUrlService extends BaseService<ProjectUrl, Integer> {
    /**
     * 根据条件查询项目URL列表
     * @param pageSize 每页大小
     * @param pageNum 页码
     * @param projectName 项目名称
     * @param projectInterfaceName 接口名称
     * @return 分页结果
     */
    PageInfo<ProjectUrl> getProjectUrlList(Integer pageSize, Integer pageNum, String projectName, String projectInterfaceName);
    void removeProjectUrl(ProjectUrl projectUrl);
    void updateProjectUrl(ProjectUrl projectUrl);
    List<ProjectUrl> getProjectUrlList();
}
