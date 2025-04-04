package com.david.hlp.Spring.repeater.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.david.hlp.Spring.repeater.module.entity.RouterUrl;
import java.util.List;
import com.david.hlp.Spring.repeater.module.dto.RouterProjectDTO;
@Mapper
public interface RouterUrlMapper {
    /**
     * 插入路由信息
     * @param routerUrl 路由信息
     */
    void insertRouterUrl(RouterUrl routerUrl);
    
    /**
     * 统计路由数量
     * @param routerUrl 查询条件
     * @return 路由数量
     */
    Long countRouterUrl(RouterUrl routerUrl);
    
    /**
     * 分页查询路由列表
     * @param routerUrl 查询条件
     * @param pageSize 每页大小
     * @param offset 偏移量
     * @return 路由列表
     */
    List<RouterProjectDTO> listRouterUrl(@Param("routerUrl") RouterUrl routerUrl, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);
    
    /**
     * 更新路由信息
     * @param routerUrl 路由信息
     */
    void updateRouterUrl(RouterUrl routerUrl);
    
    /**
     * 删除路由信息
     * @param routerUrl 路由信息
     */
    void deleteRouterUrl(RouterUrl routerUrl);
    
    /**
     * 查询所有路由信息
     * @return 所有路由信息列表
     */
    List<RouterUrl> listAllRouterUrl();
}
