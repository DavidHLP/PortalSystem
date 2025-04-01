package com.david.hlp.Spring.repeater.service.impl;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import com.david.hlp.Spring.repeater.mapper.RoleUrlMapper;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.repeater.module.entity.RoleUrl;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleUrlServiceImpl {
    private final RoleUrlMapper roleUrlMapper;
    
    /**
     * 分页查询角色URL
     *
     * @param pageInfo 分页信息
     * @return 分页结果
     */
    public PageInfo<RoleUrl> listRoleUrlByPage(PageInfo<RoleUrl> pageInfo) {
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
    public void addRoleUrl(RoleUrl roleUrl) {
        roleUrlMapper.insertRoleUrl(roleUrl);
    }
    
    /**
     * 更新角色URL
     *
     * @param roleUrl 角色URL信息
     */
    public void updateRoleUrl(RoleUrl roleUrl) {
        roleUrlMapper.updateRoleUrl(roleUrl);
    }
    
    /**
     * 删除角色URL
     *
     * @param roleUrl 角色URL信息
     */
    public void deleteRoleUrl(RoleUrl roleUrl) {
        roleUrlMapper.deleteRoleUrl(roleUrl);
    }
}
