package com.david.hlp.Spring.repeater.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import com.david.hlp.Spring.repeater.mapper.RouterUrlMapper;
import com.david.hlp.Spring.repeater.module.entity.RouterProject;
import com.david.hlp.Spring.repeater.module.entity.RouterUrl;
import com.david.hlp.Spring.common.result.PageInfo;
import java.util.List;
import com.david.hlp.Spring.repeater.module.dto.RouterProjectDTO;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RouterUrlServiceImpl {
    private final RouterUrlMapper routerUrlMapper;

    /**
     * 添加路由信息
     *
     * @param routerUrl 路由信息
     */
    @Transactional
    public void addRouterUrl(RouterProjectDTO routerUrl) {
        routerUrlMapper.insertRouterUrl(routerUrl);
        Long routerId = routerUrlMapper.selectRouterIdByUniqueId(routerUrl.getUniqueId());
        routerUrl.setId(routerId);
        updateRouterProject(routerUrl);
    }

    /**
     * 分页查询路由信息列表
     *
     * @param pageInfo 分页查询条件
     * @return 分页结果
     */
    public PageInfo<RouterProjectDTO> listRouterUrlByPage(PageInfo<RouterProjectDTO> pageInfo) {
        Integer pageNum = pageInfo.getPageNum();
        Integer pageSize = pageInfo.getPageSize();
        // 计算偏移量
        Integer offset = pageSize * (pageNum - 1);

        // 查询总数
        Long total = routerUrlMapper.countRouterUrl(pageInfo.getItem());

        // 查询数据列表
        List<RouterProjectDTO> routerUrlList = routerUrlMapper.listRouterUrl(pageInfo.getItem(), pageSize, offset);

        // 计算总页数
        Integer pages = (int) Math.ceil((double) total / pageSize);

        // 构建返回结果
        return PageInfo.<RouterProjectDTO>builder()
                .items(routerUrlList)
                .item(pageInfo.getItem())
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(total)
                .pages(pages)
                .build();
    }

    /**
     * 更新路由信息
     *
     * @param routerUrl 路由信息
     */
    @Transactional
    public void updateRouterUrl(RouterProjectDTO routerUrl) {
        routerUrlMapper.updateRouterUrl(routerUrl);
        updateRouterProject(routerUrl);
    }

    public void updateRouterProject(RouterProjectDTO routerUrl) {
        List<RouterProject> routerProjects = routerUrlMapper.listRouterProjectById(routerUrl.getId());
        List<RouterProject> newRouterProjects = routerUrl.getProjects().stream()
                .map(project -> RouterProject.builder()
                        .routerId(routerUrl.getId())
                        .projectId(project.getId())
                        .build())
                .collect(Collectors.toList());

        // 需要删除的关联关系：routerProjects中有但newRouterProjects中没有的
        List<RouterProject> deleteList = routerProjects.stream()
                .filter(rp -> newRouterProjects.stream()
                        .noneMatch(newRp ->
                            (newRp.getProjectId() != null && rp.getProjectId() != null && newRp.getProjectId().equals(rp.getProjectId())) &&
                            (newRp.getRouterId() != null && rp.getRouterId() != null && newRp.getRouterId().equals(rp.getRouterId()))))
                .collect(Collectors.toList());
        // 需要新增的关联关系：newRouterProjects中有但routerProjects中没有的
        List<RouterProject> insertList = newRouterProjects.stream()
                .filter(newRp -> routerProjects.stream()
                        .noneMatch(rp ->
                            (rp.getProjectId() != null && newRp.getProjectId() != null && rp.getProjectId().equals(newRp.getProjectId())) &&
                            (rp.getRouterId() != null && newRp.getRouterId() != null && rp.getRouterId().equals(newRp.getRouterId()))))
                .collect(Collectors.toList());
        System.out.println("insertList:"+insertList);
        if (!deleteList.isEmpty()) {
            routerUrlMapper.deleteRouterProject(deleteList);
        }
        if (!insertList.isEmpty()) {
            routerUrlMapper.insertRouterProject(insertList);
        }
    }
    /**
     * 删除路由信息
     *
     * @param routerUrl 路由信息
     */
    public void deleteRouterUrl(RouterUrl routerUrl) {
        routerUrlMapper.deleteRouterUrl(routerUrl);
    }

    /**
     * 查询所有路由信息
     * 
     * @return 所有路由信息
     */
    public List<RouterUrl> listAllRouterUrl() {
        return routerUrlMapper.listAllRouterUrl();
    }

    /**
     * 查询项目下的所有路由信息
     * 
     * @param id 项目ID
     * @return 项目下的所有路由信息
     */
    public List<RouterUrl> listRouterUrlByProjectId(Integer id) {
        return routerUrlMapper.listRouterUrlByProjectId(id);
    }
}
