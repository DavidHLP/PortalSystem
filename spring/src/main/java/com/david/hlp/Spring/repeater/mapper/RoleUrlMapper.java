package com.david.hlp.Spring.repeater.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.david.hlp.Spring.repeater.entity.RoleUrl;
import org.apache.ibatis.annotations.Param;

/**
 * 角色URL映射接口
 *
 * @author david
 */
@Mapper
public interface RoleUrlMapper extends BaseMapper<RoleUrl> {
    /**
     * 获取所有角色URL及其关联的项目信息
     *
     * @param entity 角色URL实体
     * @param pageSize 每页大小
     * @param offset 偏移量
     * @return 角色URL列表，包含项目信息
     */
    List<RoleUrl> getPage(@Param("entity") RoleUrl entity,
                                          @Param("pageSize") Integer pageSize,
                                          @Param("offset") Integer offset);

    /**
     * 获取总记录数
     *
     * @param entity 角色URL实体
     * @return 总记录数
     */
    Integer getTotalCount(@Param("entity") RoleUrl entity);

    /**
     * 根据ID删除角色URL
     *
     * @param id 角色URL ID
     * @return 影响行数
     */
    Integer deleteById(@Param("id") Integer id);

    /**
     * 更新角色URL
     *
     * @param entity 角色URL实体
     * @return 影响行数
     */
    int updateById(RoleUrl entity);

    /**
     * 更新角色URL描述
     *
     * @param entity 角色URL实体
     * @return 影响行数
     */
    int updateByIdAndDescription(RoleUrl entity);

    /**
     * 插入角色URL
     *
     * @param entity 角色URL实体
     * @return 影响行数
     */
    int insert(RoleUrl entity);

    /**
     * 根据角色ID获取角色URL列表
     *
     * @param roleId 角色ID
     * @return 角色URL列表
     */
    List<Integer> getRoleUrlListByRoleId(@Param("roleId") Integer roleId);

    /**
     * 获取所有角色URL
     *
     * @return 角色URL列表
     */
    List<RoleUrl> listAll();
}

