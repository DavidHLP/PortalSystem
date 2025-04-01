package com.david.hlp.Spring.repeater.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import com.david.hlp.Spring.repeater.mapper.RouterUrlMapper;
import com.david.hlp.Spring.repeater.module.entity.RouterUrl;
import com.david.hlp.Spring.common.result.PageInfo;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouterUrlServiceImpl {
    private final RouterUrlMapper routerUrlMapper;
    
    /**
     * 添加路由信息
     * @param routerUrl 路由信息
     */
    public void addRouterUrl(RouterUrl routerUrl) {
        routerUrlMapper.insertRouterUrl(routerUrl);
    }
    
    /**
     * 分页查询路由信息列表
     * @param pageInfo 分页查询条件
     * @return 分页结果
     */
    public PageInfo<RouterUrl> listRouterUrlByPage(PageInfo<RouterUrl> pageInfo) {
        Integer pageNum = pageInfo.getPageNum();
        Integer pageSize = pageInfo.getPageSize();
        // 计算偏移量
        Integer offset = pageSize * (pageNum - 1);
        
        // 查询总数
        Long total = routerUrlMapper.countRouterUrl(pageInfo.getItem());
        
        // 查询数据列表
        List<RouterUrl> routerUrlList = routerUrlMapper.listRouterUrl(pageInfo.getItem(), pageSize, offset);
        
        // 计算总页数
        Integer pages = (int)Math.ceil((double)total / pageSize);
        
        // 构建返回结果
        return PageInfo.<RouterUrl>builder()
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
     * @param routerUrl 路由信息
     */
    public void updateRouterUrl(RouterUrl routerUrl) {
        routerUrlMapper.updateRouterUrl(routerUrl);
    }
    
    /**
     * 删除路由信息
     * @param routerUrl 路由信息
     */
    public void deleteRouterUrl(RouterUrl routerUrl) {
        routerUrlMapper.deleteRouterUrl(routerUrl);
    }
    
    /**
     * 查询所有路由信息
     * @return 所有路由信息
     */
    public List<RouterUrl> listAllRouterUrl() {
        return routerUrlMapper.listAllRouterUrl();
    }
}
