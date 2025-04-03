package com.david.hlp.Spring.repeater.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.david.hlp.Spring.repeater.module.entity.UserRoleProject;
import com.david.hlp.Spring.repeater.module.dto.UserRoleProjectDTO;
import java.util.List;


/**
 * 用户URL映射器
 */
@Mapper
public interface UserUrlMapper {
    /**
     * 分页查询用户角色项目关联信息
     *
     * @param userRoleProject 查询条件
     * @param pageSize 页大小
     * @param offset 偏移量
     * @return 用户角色项目关联信息列表
     */
    List<UserRoleProject> listUserRoleProject(@Param("userRoleProject") UserRoleProject userRoleProject,
                                              @Param("pageSize") Integer pageSize,
                                              @Param("offset") Integer offset);

    /**
     * 查询用户角色项目关联信息总数
     *
     * @param userRoleProject 查询条件
     * @return 总数
     */
    Long countUserRoleProject(@Param("userRoleProject") UserRoleProject userRoleProject);

    /**
     * 插入用户角色项目关联信息
     *
     * @param userRoleProjectDTO 用户角色项目关联信息
     */
    void insertUserRoleProject(@Param("userRoleProject") UserRoleProjectDTO userRoleProjectDTO);

    /**
     * 删除用户角色项目关联信息
     *
     * @param userRoleProjectDTO 用户角色项目关联信息
     */
    void deleteUser(@Param("id") Long id);

    /**
     * 更新用户角色项目关联信息
     *
     * @param userRoleProjectDTO 用户角色项目关联信息
     */
    void updateUser(@Param("userRoleProject") UserRoleProject userRoleProjectDTO);

    /**
     * 更新用户密码
     *
     * @param userRoleProjectDTO 用户角色项目关联信息
     */
    void updateUserPassword(@Param("userRoleProject") UserRoleProjectDTO userRoleProjectDTO);

    /**
     * 查询用户ID
     *
     * @param userRoleProjectDTO 用户角色项目关联信息
     * @return 用户ID
     */
    Long selectUserId(@Param("userRoleProject") UserRoleProjectDTO userRoleProjectDTO);
}
