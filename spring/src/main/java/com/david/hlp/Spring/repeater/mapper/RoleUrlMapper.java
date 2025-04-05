package com.david.hlp.Spring.repeater.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.repeater.module.entity.RoleUrl;
import com.david.hlp.Spring.repeater.module.entity.UserRole;
import com.david.hlp.Spring.repeater.module.dto.RoleUrlDTO;
@Mapper
public interface RoleUrlMapper {
    /**
     * 统计角色URL记录数
     *
     * @param roleUrl 查询条件
     * @return 记录数
     */
    long countRoleUrl(RoleUrl roleUrl);

    /**
     * 分页查询角色URL列表
     *
     * @param pageInfo 分页参数
     * @return 角色URL列表
     */
    List<RoleUrlDTO> listRoleUrl(PageInfo<RoleUrlDTO> pageInfo);

    /**
     * 新增角色URL
     *
     * @param roleUrl 角色URL信息
     * @return 影响行数
     */
    int insertRoleUrl(RoleUrl roleUrl);

    /**
     * 更新角色URL
     *
     * @param roleUrl 角色URL信息
     * @return 影响行数
     */
    int updateRoleUrl(RoleUrl roleUrl);

    /**
     * 删除角色URL
     *
     * @param roleUrl 角色URL信息
     * @return 影响行数
     */
    int deleteRoleUrl(RoleUrl roleUrl);

    /**
     * 插入用户角色项目关联信息
     *
     * @param userRole 用户角色项目关联信息
     */
    void insertUserRole(UserRole userRole);

    /**
     * 删除用户角色项目关联信息
     *
     * @param userRole 用户角色项目关联信息
     */
    void deleteUserRole(UserRole userRole);


    /**
     * 更新用户角色项目关联信息
     *
     * @param userRole 用户角色项目关联信息
     */
    void updateUserRole(UserRole userRole);

    /**
     * 获取角色列表
     *
     * @return 角色列表
     */
    List<RoleUrl> getRoleList();

    /**
     * 禁用角色URL
     *
     * @param id 角色URLID
     */
    void disableRoleUrl(Long id);

    /**
     * 启用角色URL
     *
     * @param id 角色URLID
     */
    void enableRoleUrl(Long id);
}