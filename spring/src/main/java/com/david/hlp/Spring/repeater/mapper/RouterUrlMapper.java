package com.david.hlp.Spring.repeater.mapper;

import com.david.hlp.Spring.repeater.entity.RouterUrl;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 路由URL数据访问接口
 *
 * @author david
 * @date 2024-03-21
 */
@Mapper
public interface RouterUrlMapper {

    /**
     * 查询所有路由URL记录
     *
     * @return 路由URL列表
     */
    @Select("SELECT id, path, created_at FROM router_url")
    List<RouterUrl> listRouterUrls();

    /**
     * 根据ID查询路由URL记录
     *
     * @param id 路由ID
     * @return 路由URL记录
     */
    @Select("SELECT id, path, created_at FROM router_url WHERE id = #{id}")
    RouterUrl getRouterUrlById(@Param("id") Integer id);

    /**
     * 新增路由URL记录
     *
     * @param routerUrl 路由URL对象
     * @return 影响行数
     */
    @Insert("INSERT INTO router_url(path, created_at) VALUES(#{path}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(RouterUrl routerUrl);

    /**
     * 更新路由URL记录
     *
     * @param routerUrl 路由URL对象
     * @return 影响行数
     */
    @Update("UPDATE router_url SET path = #{path}, created_at = NOW() WHERE id = #{id}")
    int update(RouterUrl routerUrl);

    /**
     * 根据ID删除路由URL记录
     *
     * @param id 路由ID
     * @return 影响行数
     */
    @Delete("DELETE FROM router_url WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);

    /**
     * 获取所有路由信息（不包含创建时间）
     *
     * @return 路由列表
     */
    @Select("SELECT id, path FROM router_url")
    List<RouterUrl> listRouterUrlsWithoutCreatedAt();

    /**
     * 根据ID查询路由信息（不包含创建时间）
     *
     * @param id 路由ID
     * @return 路由信息
     */
    @Select("SELECT id, path FROM router_url WHERE id = #{id}")
    RouterUrl getRouterUrlByIdWithoutCreatedAt(@Param("id") Integer id);
}
