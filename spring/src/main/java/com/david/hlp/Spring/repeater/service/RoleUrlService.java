package com.david.hlp.Spring.repeater.service;

import com.david.hlp.Spring.repeater.entity.RoleUrl;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.repeater.entity.Url;
import java.util.List;
/**
 * 角色URL服务接口
 */
public interface RoleUrlService {

    /**
     * 创建角色URL
     * @param entity 角色URL实体
     * @return 创建后的角色URL实体
     */
    RoleUrl create(RoleUrl entity);

    /**
     * 根据ID获取角色URL
     * @param id 角色URL的ID
     * @return 角色URL实体
     */
    RoleUrl getById(Integer id);

    /**
     * 获取所有角色URL
     * @return 角色URL列表
     */
    List<RoleUrl> listAll();

    /**
     * 根据ID删除角色URL
     * @param id 角色URL的ID
     */
    void deleteById(Integer id);

    /**
     * 更新角色URL
     * @param entity 角色URL实体
     * @return 更新后的角色URL实体
     */
    RoleUrl update(RoleUrl entity);

    /**
     * 更新角色URL描述
     * @param entity 角色URL实体
     * @return 更新后的角色URL实体
     */
    RoleUrl updateByIdAndDescription(RoleUrl entity);

    /**
     * 获取角色URL分页信息
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param entity 角色URL实体
     * @return 角色URL分页信息
     */
    PageInfo<RoleUrl> getPage(Integer pageNum, Integer pageSize, RoleUrl entity);

    /**
     * 根据角色ID获取角色URL列表
     * @param roleId 角色ID
     * @return 角色URL列表
     */
    List<Url> getRoleUrlListByRoleId(Integer roleId);

    /**
     * 批量添加角色URL
     * @param roleId 角色ID
     * @param urlIds 角色URL的ID列表
     */
    void batchAddUrls(Integer roleId, List<Integer> urlIds);

    /**
     * 删除角色URL
     * @param roleId 角色ID
     * @param urlId 角色URL的ID
     */
    void deleteRoleUrl(Integer roleId, Integer urlId);
}