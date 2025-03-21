package com.david.hlp.Spring.repeater.mapper;

import com.david.hlp.Spring.repeater.entity.RouterUrl;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface RouterUrlMapper {
    
    @Select("SELECT * FROM router_url")
    List<RouterUrl> findAll();
    
    @Select("SELECT * FROM router_url WHERE id = #{id}")
    RouterUrl findById(@Param("id") Integer id);
    
    @Insert("INSERT INTO router_url(path, created_at) VALUES(#{path}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(RouterUrl routerUrl);
    
    @Update("UPDATE router_url SET path = #{path} WHERE id = #{id}")
    int update(RouterUrl routerUrl);
    
    @Delete("DELETE FROM router_url WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);

    /**
     * 获取所有路由信息
     * 不包含创建时间
     * @return 路由列表
     */
    @Select("SELECT id, path FROM router_url")
    List<RouterUrl> listAll();

    /**
     * 根据ID查询路由
     * 不包含创建时间
     */
    @Select("SELECT id, path FROM router_url WHERE id = #{id}")
    RouterUrl findByIdHasNoCreatedAt(@Param("id") Integer id);
}
