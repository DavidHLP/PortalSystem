package com.david.hlp.Spring.repeater.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.david.hlp.Spring.repeater.entity.RoleUrlUrl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RoleUrlUrlMapper extends BaseMapper<RoleUrlUrl> {
    /**
     * 批量插入角色URL关联
     * @param roleUrlUrls 角色URL关联列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<RoleUrlUrl> roleUrlUrls);

    /**
     * 根据角色ID和URL ID删除关联
     * @param roleId 角色ID
     * @param urlId URL ID
     * @return 影响行数
     */
    int deleteByRoleIdAndUrlId(@Param("roleId") Integer roleId, @Param("urlId") Integer urlId);
} 