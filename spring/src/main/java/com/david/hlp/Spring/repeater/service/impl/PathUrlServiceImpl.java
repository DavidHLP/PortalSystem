package com.david.hlp.Spring.repeater.service.impl;

import com.david.hlp.Spring.repeater.entity.ProjectUrlRequest;
import com.david.hlp.Spring.repeater.entity.Url;
import com.david.hlp.Spring.repeater.mapper.PathUrlMapper;
import com.david.hlp.Spring.repeater.mapper.RouterUrlMapper;
import com.david.hlp.Spring.repeater.mapper.HostMapper;
import com.david.hlp.Spring.repeater.mapper.PortMapper;
import com.david.hlp.Spring.repeater.mapper.ProjectMapper;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
@Slf4j
@Service
@RequiredArgsConstructor
public class PathUrlServiceImpl implements PathUrlService {
    private final PathUrlMapper pathUrlMapper;
    private final HostMapper hostMapper;
    private final PortMapper portMapper;
    private final RouterUrlMapper routerMapper;
    private final ProjectMapper projectMapper;

    private static final String HTTP_PROTOCOL = "http";
    private static final String HTTPS_PROTOCOL = "https";
    private static final String POST_METHOD = "POST";
    private static final String GET_METHOD = "GET";
    private static final String PUT_METHOD = "PUT";
    private static final String DELETE_METHOD = "DELETE";
    /**
     * 根据项目ID获取URL列表
     *
     * @param projectId 项目ID
     * @return URL列表
     */
    @Override
    public List<Url> getByProjectId(Integer projectId) {
        List<Url> urls = pathUrlMapper.getByProjectId(projectId);
        fillUrlRelatedInfo(urls);
        return urls;
    }
   /**
     * 根据项目ID获取URL列表
     *
     * @param projectId 项目ID
     * @return URL列表
     */
    @Override
    public List<Url> getByListUrlId(List<Integer> urlIds) {
        List<Url> urls = new ArrayList<>();
        for (Integer urlId : urlIds) {
            Url url = pathUrlMapper.getByUrlId(urlId);
            urls.add(url);
        }
        fillUrlRelatedInfo(urls);
        return urls;
    }

    /**
     * 根据ID获取路径URL
     *
     * @param id 路径URL ID
     * @return 路径URL信息
     * @throws BusinessException 当ID为空或URL不存在时抛出异    常
     */
    @Override
    public Url getById(Integer id) {
        log.info("获取路径URL信息, id: {}", id);
        if (id == null) {
            throw new BusinessException("路径URL ID不能为空");
        }
        Url url = pathUrlMapper.getById(id);
        if (url == null) {
            throw new BusinessException("路径URL不存在");
        }
        return url;
    }

    /**
     * 分页查询路径URL
     *
     * @param page 页码
     * @param limit 每页大小
     * @param entity 查询条件
     * @return 分页结果
     */
    @Override
    public PageInfo<Url> getPage(Integer page, Integer limit, Url entity) {
        return null;
    }

    /**
     * 获取所有路径URL
     *
     * @return 路径URL列表
     */
    @Override
    public List<Url> listAll() {
        log.info("获取所有路径URL列表");
        List<Url> urls = pathUrlMapper.listAll();
        fillUrlRelatedInfo(urls);
        return urls;
    }

    /**
     * 分页查询路径URL
     *
     * @param page 页码
     * @param limit 每页大小
     * @param entity 查询条件
     * @return 分页结果
     */
    @Override
    public PageInfo<ProjectUrlRequest> getPageUltra(Integer page, Integer limit, Url entity) {
        log.info("分页查询路径URL, page: {}, limit: {}", page, limit);
        validatePageParams(page, limit);

        int offset = (page - 1) * limit;
        List<Url> urls = pathUrlMapper.listByConditions(
            entity.getRouterId(),
            entity.getHostId(),
            entity.getPortId(),
            entity.getProjectId(),
            entity.getMethod(),
            entity.getIsActive() != null ? entity.getIsActive() ? 1 : 0 : null,
            entity.getProtocol(),
            offset,
            limit
        );
        long total = pathUrlMapper.countByConditions(
            entity.getRouterId(),
            entity.getHostId(),
            entity.getPortId(),
            entity.getProjectId(),
            entity.getMethod(),
            entity.getIsActive() != null ? entity.getIsActive() ? 1 : 0 : null,
            entity.getProtocol()
        );

        fillUrlRelatedInfo(urls);
        int pages = (int) Math.ceil((double) total / limit);

        HashMap<String, ProjectUrlRequest> projectUrlRequestMap = new HashMap<>();
        for (Url url : urls) {
            String projectName = url.getProject().getProjectName();
            ProjectUrlRequest request = projectUrlRequestMap.computeIfAbsent(projectName, k -> createProjectUrlRequest(url));
            request.getUrls().add(url);
            updateRequestCounts(request, url);
        }

        List<ProjectUrlRequest> projectUrlRequests = new ArrayList<>(projectUrlRequestMap.values());

        return PageInfo.<ProjectUrlRequest>builder()
            .items(projectUrlRequests)
            .pageNum(page)
            .pageSize(limit)
            .total(total)
            .pages(pages)
            .hasNext(page < pages)
            .hasPrevious(page > 1)
            .isFirst(page == 1)
            .isLast(page >= pages)
            .build();
    }

    /**
     * 获取项目URL请求列表
     *
     * @param page 页码
     * @param limit 每页大小
     * @param routerId 路由ID
     * @param hostId 主机ID
     * @param portId 端口ID
     * @param projectId 项目ID
     * @param method 请求方法
     * @param isActive 是否激活
     * @param protocol 协议
     * @return 分页结果
     */
    @Override
    public PageInfo<ProjectUrlRequest> getProjectUrlRequestList(Integer page, Integer limit, Integer routerId, Integer hostId, Integer portId, Integer projectId, String method, Integer isActive, String protocol) {
        log.info("获取项目URL请求列表, page: {}, limit: {}, method: {}, protocol: {}", page, limit, method, protocol);
        validatePageParams(page, limit);

        int offset = (page - 1) * limit;
        List<Url> urls = pathUrlMapper.listByConditions(routerId, hostId, portId, projectId, method, isActive, protocol, offset, limit);
        fillUrlRelatedInfo(urls);

        HashMap<String, ProjectUrlRequest> projectUrlRequestMap = new HashMap<>();
        for (Url url : urls) {
            String projectName = url.getProject().getProjectName();
            ProjectUrlRequest request = projectUrlRequestMap.computeIfAbsent(projectName, k -> createProjectUrlRequest(url));
            request.getUrls().add(url);
            updateRequestCounts(request, url);
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
     * @throws BusinessException 当URL信息无效时抛出异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Url create(Url url) {
        log.info("创建路径URL: {}", url);
        validateUrl(url);
        pathUrlMapper.insert(url);
        return url;
    }

    /**
     * 更新路径URL
     *
     * @param url 路径URL信息
     * @return 更新后的路径URL信息
     * @throws BusinessException 当URL信息无效时抛出异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Url update(Url url) {
        log.info("更新路径URL: {}", url);
        validateUrl(url);
        if (url.getId() == null) {
            throw new BusinessException("路径URL ID不能为空");
        }
        pathUrlMapper.update(url);
        return url;
    }

    /**
     * 删除路径URL
     *
     * @param id 路径URL ID
     * @throws BusinessException 当ID为空时抛出异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        log.info("删除路径URL, id: {}", id);
        if (id == null) {
            throw new BusinessException("路径URL ID不能为空");
        }
        pathUrlMapper.deleteById(id);
    }

    /**
     * 填充URL关联信息
     *
     * @param urls URL列表
     */
    private void fillUrlRelatedInfo(List<Url> urls) {
        for (Url url : urls) {
            url.setHost(hostMapper.getHostBasicInfoById(url.getHostId()));
            url.setPort(portMapper.getPortByIdWithoutDescription(url.getPortId()));
            url.setRouter(routerMapper.getRouterUrlByIdWithoutCreatedAt(url.getRouterId()));
            url.setProject(projectMapper.getProjectBasicInfoById(url.getProjectId()));
        }
    }

    /**
     * 创建项目URL请求对象
     *
     * @param url URL信息
     * @return 项目URL请求对象
     */
    private ProjectUrlRequest createProjectUrlRequest(Url url) {
        return ProjectUrlRequest.builder()
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
    }

    /**
     * 更新请求计数
     *
     * @param request 项目URL请求对象
     * @param url URL信息
     */
    private void updateRequestCounts(ProjectUrlRequest request, Url url) {
        switch (url.getMethod()) {
            case POST_METHOD:
                request.setPostCount(request.getPostCount() + 1);
                break;
            case GET_METHOD:
                request.setGetCount(request.getGetCount() + 1);
                break;
            case PUT_METHOD:
                request.setPutCount(request.getPutCount() + 1);
                break;
            case DELETE_METHOD:
                request.setDeleteCount(request.getDeleteCount() + 1);
                break;
        }
        switch (url.getProtocol()) {
            case HTTP_PROTOCOL:
                request.setHttpCount(request.getHttpCount() + 1);
                break;
            case HTTPS_PROTOCOL:
                request.setHttpsCount(request.getHttpsCount() + 1);
                break;
        }
    }

    /**
     * 验证分页参数
     *
     * @param page 页码
     * @param limit 每页大小
     */
    private void validatePageParams(Integer page, Integer limit) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (limit == null || limit < 1) {
            limit = 10;
        }
    }

    /**
     * 验证URL信息
     *
     * @param url URL信息
     * @throws BusinessException 当URL信息无效时抛出异常
     */
    private void validateUrl(Url url) {
        if (url == null) {
            throw new BusinessException("URL信息不能为空");
        }
        if (!StringUtils.hasText(url.getProtocol())) {
            throw new BusinessException("协议不能为空");
        }
        if (!StringUtils.hasText(url.getMethod())) {
            throw new BusinessException("请求方法不能为空");
        }
        if (url.getHostId() == null) {
            throw new BusinessException("主机ID不能为空");
        }
        if (url.getPortId() == null) {
            throw new BusinessException("端口ID不能为空");
        }
        if (url.getRouterId() == null) {
            throw new BusinessException("路由ID不能为空");
        }
        if (url.getProjectId() == null) {
            throw new BusinessException("项目ID不能为空");
        }
    }
}
