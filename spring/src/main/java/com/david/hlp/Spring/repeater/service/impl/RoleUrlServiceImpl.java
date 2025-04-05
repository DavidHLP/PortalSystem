package com.david.hlp.Spring.repeater.service.impl;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import com.david.hlp.Spring.repeater.mapper.RoleUrlMapper;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.repeater.module.entity.RoleUrl;
import java.util.List;
import com.david.hlp.Spring.repeater.module.dto.RoleUrlDTO;
import com.david.hlp.Spring.repeater.module.entity.RouterRole;

import jakarta.transaction.Transactional;

import com.david.hlp.Spring.repeater.mapper.RouterUrlMapper;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleUrlServiceImpl {
    private final RoleUrlMapper roleUrlMapper;
    private final RouterUrlMapper routerUrlMapper;
    /**
     * 分页查询角色URL
     *
     * @param pageInfo 分页信息
     * @return 分页结果
     */
    public PageInfo<RoleUrlDTO> listRoleUrlByPage(PageInfo<RoleUrlDTO> pageInfo) {
        long total = roleUrlMapper.countRoleUrl(pageInfo.getItem());
        pageInfo.setTotal(total);
        pageInfo.setPageNum(pageInfo.getPageNum() - 1);
        if (total > 0) {
            pageInfo.setItems(roleUrlMapper.listRoleUrl(pageInfo));
        }
        return pageInfo;
    }

    /**
     * 添加角色URL
     *
     * @param roleUrl 角色URL信息
     */
    @Transactional
    public void addRoleUrl(RoleUrlDTO roleUrl) {
        roleUrlMapper.insertRoleUrl(roleUrl);
        List<RouterRole> newRouterRoles = roleUrl.getRouters().stream()
                .map(router -> RouterRole.builder()
                        .routerId(router.getId())
                        .roleId(roleUrl.getId())
                        .build())
                .collect(Collectors.toList());
        List<RouterRole> oldRouterRoles = routerUrlMapper.listRouterRoleByRoleId(roleUrl.getId());
        updateRouterRole(newRouterRoles, oldRouterRoles);
    }

    /**
     * 更新角色URL
     *
     * @param roleUrl 角色URL信息
     */
    @Transactional
    public void updateRoleUrl(RoleUrlDTO roleUrl) {
        roleUrlMapper.updateRoleUrl(roleUrl);
        List<RouterRole> newRouterRoles = roleUrl.getRouters().stream()
                .map(router -> RouterRole.builder()
                        .routerId(router.getId())
                        .roleId(roleUrl.getId())
                        .build())
                .collect(Collectors.toList());
        List<RouterRole> oldRouterRoles = routerUrlMapper.listRouterRoleByRoleId(roleUrl.getId());
        updateRouterRole(newRouterRoles, oldRouterRoles);
    }

    private void updateRouterRole(List<RouterRole> newRouterRoles, List<RouterRole> oldRouterRoles) {
        List<RouterRole> deleteList = oldRouterRoles.stream()
                .filter(oldRouterRole -> !newRouterRoles.contains(oldRouterRole))
                .collect(Collectors.toList());
        List<RouterRole> addList = newRouterRoles.stream()
                .filter(newRouterRole -> !oldRouterRoles.contains(newRouterRole))
                .collect(Collectors.toList());
        for (RouterRole routerRole : deleteList) {
            routerUrlMapper.deleteRouterRole(routerRole.getRouterId(), routerRole.getRoleId());
        }
        for (RouterRole routerRole : addList) {
            routerUrlMapper.insertRouterRole(routerRole.getRouterId(), routerRole.getRoleId());
        }
    }

    /**
     * 删除角色URL
     *
     * @param roleUrl 角色URL信息
     */
    public void deleteRoleUrl(RoleUrl roleUrl) {
        roleUrlMapper.deleteRoleUrl(roleUrl);
    }

    /**
     * 获取角色列表
     *
     * @return 角色列表
     */
    public List<RoleUrl> getRoleList() {
        return roleUrlMapper.getRoleList();
    }

    /**
     * 禁用角色URL
     *
     * @param id 角色URLID
     */
    public void disableRoleUrl(Long id) {
        roleUrlMapper.disableRoleUrl(id);
    }

    /**
     * 启用角色URL
     *
     * @param id 角色URLID
     */
    public void enableRoleUrl(Long id) {
        roleUrlMapper.enableRoleUrl(id);
    }
}
