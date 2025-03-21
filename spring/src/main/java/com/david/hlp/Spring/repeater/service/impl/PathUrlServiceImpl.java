package com.david.hlp.Spring.repeater.service.impl;

import com.david.hlp.Spring.repeater.entity.ProjectUrlRequest;
import com.david.hlp.Spring.repeater.entity.Url;
import com.david.hlp.Spring.repeater.mapper.PathUrlMapper;
import com.david.hlp.Spring.repeater.mapper.RouterUrlMapper;
import com.david.hlp.Spring.repeater.mapper.HostMapper;
import com.david.hlp.Spring.repeater.mapper.PortMapper;
import com.david.hlp.Spring.repeater.mapper.ProjectMapper;
import com.david.hlp.Spring.common.result.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import com.david.hlp.Spring.repeater.service.PathUrlService;

/**
 * 路径URL服务实现类
 *
 * @author david
 * @date 2024/03/21
 */
@Service
@RequiredArgsConstructor
public class PathUrlServiceImpl implements PathUrlService {
    private final PathUrlMapper pathUrlMapper;
    private final HostMapper hostMapper;
    private final PortMapper portMapper;
    private final RouterUrlMapper routerMapper;
    private final ProjectMapper projectMapper;

    /**
     * 根据ID获取路径URL
     *
     * @param id 路径URL ID
     * @return 路径URL信息
     */
    @Override
    public Url getById(Integer id) {
        return pathUrlMapper.getById(id);
    }

    /**
     * 获取所有路径URL
     *
     * @return 路径URL列表
     */
    @Override
    public PageInfo<Url> listAll() {
        List<Url> urls = pathUrlMapper.listAll();
        for (Url url : urls) {
            url.setHost(hostMapper.getHostBasicInfoById(url.getHostId()));
            url.setPort(portMapper.getPortByIdWithoutDescription(url.getPortId()));
            url.setRouter(routerMapper.getRouterUrlByIdWithoutCreatedAt(url.getRouterId()));
            url.setProject(projectMapper.getProjectBasicInfoById(url.getProjectId()));
        }
        return PageInfo.<Url>builder()
            .items(urls)
            .pageNum(1)
            .pageSize(urls.size())
            .total((long)urls.size())
            .build();
    }

    /**
     * 分页查询路径URL
     *
     * @param page 页码
     * @param limit 每页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Url> getPage(Integer page, Integer limit) {
        int offset = (page - 1) * limit;
        List<Url> urls = pathUrlMapper.listByConditions(null, null, null, null, null, null, null, offset, limit);
        long total = pathUrlMapper.countByConditions(null, null, null, null, null, null, null);
        
        for (Url url : urls) {
            url.setHost(hostMapper.getHostBasicInfoById(url.getHostId()));
            url.setPort(portMapper.getPortByIdWithoutDescription(url.getPortId()));
            url.setRouter(routerMapper.getRouterUrlByIdWithoutCreatedAt(url.getRouterId()));
            url.setProject(projectMapper.getProjectBasicInfoById(url.getProjectId()));
        }
        
        return PageInfo.<Url>builder()
            .items(urls)
            .pageNum(page)
            .pageSize(limit)
            .total(total)
            .build();
    }

    /**
     * 获取所有项目URL请求
     *
     * @param page 页码
     * @param limit 每页数量
     * @param routerId 路由ID
     * @param hostId 主机ID
     * @param portId 端口ID
     * @param projectId 项目ID
     * @param method 方法
     * @param isActive 是否活跃
     * @param protocol 协议
     * @return 项目URL请求列表
     */
    @Override
    public PageInfo<ProjectUrlRequest> getProjectUrlRequestList(Integer page, Integer limit, Integer routerId, Integer hostId, Integer portId, Integer projectId, String method, Integer isActive, String protocol) {
        // 计算偏移量
        int offset = (page - 1) * limit;
        // 获取分页数据
        List<Url> urls = pathUrlMapper.listByConditions(routerId, hostId, portId, projectId, method, isActive, protocol, offset, limit);
        // 填充关联数据
        for (Url url : urls) {
            url.setHost(hostMapper.getHostBasicInfoById(url.getHostId()));
            url.setPort(portMapper.getPortByIdWithoutDescription(url.getPortId()));
            url.setRouter(routerMapper.getRouterUrlByIdWithoutCreatedAt(url.getRouterId()));
            url.setProject(projectMapper.getProjectBasicInfoById(url.getProjectId()));
        }
        // 构建返回结果
        HashMap<String, ProjectUrlRequest> projectUrlRequestMap = new HashMap<>();
        for (Url url : urls) {
            if (!projectUrlRequestMap.containsKey(url.getProject().getProjectName())) {
                ProjectUrlRequest projectUrlRequest = ProjectUrlRequest.builder()
                    .projectId(url.getProjectId())
                    .projectName(url.getProject().getProjectName())
                    .projectInterfaceName(url.getProject().getProjectInterfaceName())
                    .urls(new ArrayList<>())
                    .postCount(0)
                    .getCount(0)
                    .putCount(0)
                    .deleteCount(0)
                    .httpCount(0)
                    .httpsCount(0)
                    .build();
                projectUrlRequest.getUrls().add(url);
                projectUrlRequestMap.put(url.getProject().getProjectName(), projectUrlRequest);
            } else {
                projectUrlRequestMap.get(url.getProject().getProjectName()).getUrls().add(url);
            }
            switch (url.getMethod()) {
                case "POST":
                    projectUrlRequestMap.get(url.getProject().getProjectName()).setPostCount(projectUrlRequestMap.get(url.getProject().getProjectName()).getPostCount() + 1);
                    break;
                case "GET":
                    projectUrlRequestMap.get(url.getProject().getProjectName()).setGetCount(projectUrlRequestMap.get(url.getProject().getProjectName()).getGetCount() + 1);
                    break;
                case "PUT":
                    projectUrlRequestMap.get(url.getProject().getProjectName()).setPutCount(projectUrlRequestMap.get(url.getProject().getProjectName()).getPutCount() + 1);
                    break;
                case "DELETE":
                    projectUrlRequestMap.get(url.getProject().getProjectName()).setDeleteCount(projectUrlRequestMap.get(url.getProject().getProjectName()).getDeleteCount() + 1);
                    break;
            }
            switch (url.getProtocol()) {
                case "http":
                    projectUrlRequestMap.get(url.getProject().getProjectName()).setHttpCount(projectUrlRequestMap.get(url.getProject().getProjectName()).getHttpCount() + 1);
                    break;
                case "https":
                    projectUrlRequestMap.get(url.getProject().getProjectName()).setHttpsCount(projectUrlRequestMap.get(url.getProject().getProjectName()).getHttpsCount() + 1);
                    break;
            }
        }

        List<ProjectUrlRequest> projectUrlRequests = new ArrayList<>(projectUrlRequestMap.values());
        return PageInfo.<ProjectUrlRequest>builder()
            .items(projectUrlRequests)
            .pageNum(page)
            .pageSize(limit)
            .total((long)projectUrlRequests.size())
            .build();
    }

    /**
     * 创建路径URL
     *
     * @param url 路径URL信息
     * @return 创建后的路径URL信息
     */
    @Override
    @Transactional
    public Url create(Url url) {
        pathUrlMapper.insert(url);
        return url;
    }

    /**
     * 更新路径URL
     *
     * @param url 路径URL信息
     * @return 更新后的路径URL信息
     */
    @Override
    @Transactional
    public Url update(Url url) {
        pathUrlMapper.update(url);
        return url;
    }

    /**
     * 删除路径URL
     *
     * @param id 路径URL ID
     */
    @Override
    @Transactional
    public void deleteById(Integer id) {
        pathUrlMapper.deleteById(id);
    }
}
