package com.david.hlp.SpringBootWork.repeater.service;

import com.david.hlp.SpringBootWork.repeater.entity.ProjectUrlRequest;
import com.david.hlp.SpringBootWork.repeater.entity.Url;
import com.david.hlp.SpringBootWork.repeater.mapper.PathUrlMapper;
import com.david.hlp.SpringBootWork.repeater.mapper.RouterUrlMapper;
import com.david.hlp.SpringBootWork.repeater.mapper.HostMapper;
import com.david.hlp.SpringBootWork.repeater.mapper.PortMapper;
import com.david.hlp.SpringBootWork.repeater.mapper.ProjectMapper;
import com.david.hlp.SpringBootWork.common.result.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
@Service
@RequiredArgsConstructor
public class PathUrlServiceImp {
    private final PathUrlMapper pathUrlMapper;
    private final HostMapper hostMapper;
    private final PortMapper portMapper;
    private final RouterUrlMapper routerMapper;
    private final ProjectMapper projectMapper;

    public Url findById(Integer id) {
        return pathUrlMapper.findById(id);
    }

    public List<Url> findAll() {
        List<Url> urls = pathUrlMapper.findAll();
        for (Url url : urls) {
            url.setHost(hostMapper.findByIdHasNoDescription(url.getHostId()));
            url.setPort(portMapper.findByIdHasNoDescription(url.getPortId()));
            url.setRouter(routerMapper.findByIdHasNoCreatedAt(url.getRouterId()));
            url.setProject(projectMapper.findByIdHasNoDescription(url.getProjectId()));
        }
        return urls;
    }

    public PageInfo<ProjectUrlRequest> findAllProjectUrlRequest(Integer page, Integer limit, Integer routerId, Integer hostId, Integer portId, Integer projectId, String method, Integer isActive, String protocol) {
        // 计算偏移量
        int offset = (page - 1) * limit;
        // 获取分页数据
        List<Url> urls = pathUrlMapper.findByConditions(routerId, hostId, portId, projectId, method, isActive, protocol, offset, limit);
        // 获取总记录数
        long total = pathUrlMapper.countByConditions(routerId, hostId, portId, projectId, method, isActive, protocol);
        // 填充关联数据
        for (Url url : urls) {
            url.setHost(hostMapper.findByIdHasNoDescription(url.getHostId()));
            url.setPort(portMapper.findByIdHasNoDescription(url.getPortId()));
            url.setRouter(routerMapper.findByIdHasNoCreatedAt(url.getRouterId()));
            url.setProject(projectMapper.findByIdHasNoDescription(url.getProjectId()));
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

    @Transactional
    public Url create(Url url) {
        pathUrlMapper.insert(url);
        return url;
    }

    @Transactional
    public Url update(Url url) {
        pathUrlMapper.update(url);
        return url;
    }

    @Transactional
    public void deleteById(Integer id) {
        pathUrlMapper.deleteById(id);
    }
}
