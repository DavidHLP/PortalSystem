package com.david.hlp.Spring.repeater.service.impl;

import com.david.hlp.Spring.repeater.entity.RouterUrl;
import com.david.hlp.Spring.repeater.mapper.RouterUrlMapper;
import com.david.hlp.Spring.common.result.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import com.david.hlp.Spring.repeater.service.RouterUrlService;
/**
 * 路由URL服务实现类
 *
 * @author david
 * @date 2024/03/21
 */
@Service
@RequiredArgsConstructor
public class RouterUrlServiceImpl implements RouterUrlService {
    private final RouterUrlMapper routerUrlMapper;

    /**
     * 获取所有路由URL列表
     *
     * @return 路由URL列表
     */
    @Override
    public List<RouterUrl> listRouterUrls() {
        return routerUrlMapper.listRouterUrls();
    }

    /**
     * 根据ID获取路由URL
     *
     * @param id 路由URL ID
     * @return 路由URL信息
     */
    @Override
    public RouterUrl getById(Integer id) {
        return routerUrlMapper.getRouterUrlById(id);
    }

    /**
     * 创建路由URL
     *
     * @param entity 路由URL信息
     * @return 创建后的路由URL信息
     */
    @Override
    public RouterUrl create(RouterUrl entity) {
        entity.setCreatedAt(LocalDateTime.now());
        routerUrlMapper.insert(entity);
        return entity;
    }

    /**
     * 更新路由URL
     *
     * @param entity 路由URL信息
     * @return 更新后的路由URL信息
     */
    @Override
    public RouterUrl update(RouterUrl entity) {
        routerUrlMapper.update(entity);
        return entity;
    }

    /**
     * 根据ID删除路由URL
     *
     * @param id 路由URL ID
     */
    @Override
    public void deleteById(Integer id) {
        routerUrlMapper.deleteById(id);
    }

    @Override
    public PageInfo<RouterUrl> getPage(Integer page, Integer limit) {
        PageInfo<RouterUrl> pageInfo = new PageInfo<>();
        pageInfo.setItems(routerUrlMapper.listRouterUrls());
        return pageInfo;
    }

    @Override
    public PageInfo<RouterUrl> listAll() {
        return getPage(1, Integer.MAX_VALUE);
    }

    @Override
    public PageInfo<RouterUrl> getRouterUrlList() {
        return listAll();
    }
}
