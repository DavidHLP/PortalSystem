package com.david.hlp.Spring.repeater.service.impl;

import com.david.hlp.Spring.repeater.entity.RouterUrl;
import com.david.hlp.Spring.repeater.mapper.RouterUrlMapper;
import com.david.hlp.Spring.common.result.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(rollbackFor = Exception.class)
    public RouterUrl create(RouterUrl entity) {
        if (entity == null) {
            throw new IllegalArgumentException("路由URL信息不能为空");
        }
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
    @Transactional(rollbackFor = Exception.class)
    public RouterUrl update(RouterUrl entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("路由URL信息或ID不能为空");
        }
        routerUrlMapper.update(entity);
        return entity;
    }

    /**
     * 根据ID删除路由URL
     *
     * @param id 路由URL ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("路由URL ID不能为空");
        }
        routerUrlMapper.deleteById(id);
    }

    /**
     * 分页查询路由URL
     *
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param entity 查询条件
     * @return 分页结果
     */
    @Override
    public PageInfo<RouterUrl> getPage(Integer pageNum, Integer pageSize, RouterUrl entity) {
        if (pageNum == null || pageNum < 1) {
            throw new IllegalArgumentException("页码不能为空且必须大于0");
        }
        if (pageSize == null || pageSize < 1) {
            throw new IllegalArgumentException("每页大小不能为空且必须大于0");
        }
        
        int offset = (pageNum - 1) * pageSize;
        String path = entity != null ? entity.getPath() : null;
        Long total = routerUrlMapper.countRouterUrls(path);
        List<RouterUrl> list = routerUrlMapper.listRouterUrlsByPage(offset, pageSize, path);
        
        return PageInfo.<RouterUrl>builder()
                .items(list)
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(total)
                .build();
    }

    /**
     * 获取所有路由URL列表（不包含创建时间）
     *
     * @return 路由URL列表
     */
    @Override
    public List<RouterUrl> listAll() {
        return routerUrlMapper.listRouterUrlsWithoutCreatedAt();
    }
}
