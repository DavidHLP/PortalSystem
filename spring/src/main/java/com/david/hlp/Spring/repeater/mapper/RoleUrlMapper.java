package com.david.hlp.Spring.repeater.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.david.hlp.Spring.repeater.entity.RoleUrl;

@Mapper
public interface RoleUrlMapper extends BaseMapper<RoleUrl> {
    /**
     * 获取所有角色URL
     * @return 角色URL列表
     */
    @Select("SELECT id, project_id, role_name, description FROM role_url")
    List<RoleUrl> listAll();

    /**
     * 根据ID获取角色URL
     * @param id 角色URL ID
     * @return 角色URL
     */
    RoleUrl getById(Integer id);
}
