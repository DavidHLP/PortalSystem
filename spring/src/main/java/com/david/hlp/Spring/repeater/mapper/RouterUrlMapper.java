package com.david.hlp.Spring.repeater.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.david.hlp.Spring.repeater.module.entity.RouterUrl;
import java.util.List;
import com.david.hlp.Spring.repeater.module.dto.RouterProjectDTO;
import com.david.hlp.Spring.repeater.module.entity.RouterProject;
import com.david.hlp.Spring.repeater.module.entity.RouterRole;
@Mapper
public interface RouterUrlMapper {
    /**
     * 插入路由信息
     * @param routerUrl 路由信息
     */
    void insertRouterUrl(RouterProjectDTO routerUrl);

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
    void updateRouterUrl(RouterProjectDTO routerUrl);
        /**
     * 删除路由信息
     * @param routerUrl 路由信息
     */
    void deleteRouterUrl(RouterUrl routerUrl);
        /**
     * 查询所有路由信息
     * @return List<RouterUrl> 所有路由信息列表
     */
    List<RouterUrl> listAllRouterUrl();

    /**
     * 根据ID查询路由信息
     * @param id 路由ID
     * @return List<RouterProject> 路由项目列表
     */
    List<RouterProject> listRouterProjectById(@Param("id") Long id);

    /**
     * 删除路由项目关联关系
     * @param routerProjects 路由项目关联关系列表
     */
    void deleteRouterProject(List<RouterProject> routerProjects);

    /**
     * 插入路由项目关联关系
     * @param routerProjects 路由项目关联关系列表
     */
    void insertRouterProject(List<RouterProject> routerProjects);

    /**
     * 根据uniqueId查询路由信息
     * @param uniqueId 唯一标识
     * @return 路由信息
     */
    Long selectRouterIdByUniqueId(@Param("uniqueId") String uniqueId);

    /**
     * 根据项目ID查询路由信息
     * @param id 项目ID
     * @return 路由信息列表
     */
    List<RouterUrl> listRouterUrlByProjectId(@Param("projectId") Integer id);

    /**
     * 删除路由角色关联关系
     * @param routerId 路由ID
     * @param roleId 角色ID
     */
    void deleteRouterRole(@Param("routerId") Long routerId, @Param("roleId") Long roleId);

    /**
     * 插入路由角色关联关系
     * @param routerId 路由ID
     * @param roleId 角色ID
     */
    void insertRouterRole(@Param("routerId") Long routerId, @Param("roleId") Long roleId);

    /**
     * 根据角色ID查询路由角色关联关系
     * @param roleId 角色ID
     * @return 路由角色关联关系列表
     */
    List<RouterRole> listRouterRoleByRoleId(@Param("roleId") Long roleId);
}
