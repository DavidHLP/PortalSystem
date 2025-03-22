package com.david.hlp.Spring.repeater.mapper;

import com.david.hlp.Spring.repeater.entity.RouterUrl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
    List<RouterUrl> listRouterUrls();

    /**
     * 分页查询路由URL记录
     *
     * @param offset 偏移量
     * @param limit 每页记录数
     * @param path 路径条件
     * @return 路由URL列表
     */
    List<RouterUrl> listRouterUrlsByPage(@Param("offset") Integer offset, @Param("limit") Integer limit, @Param("path") String path);

    /**
     * 获取总记录数
     *
     * @param path 路径条件
     * @return 总记录数
     */
    long countRouterUrls(@Param("path") String path);

    /**
     * 根据ID查询路由URL记录
     *
     * @param id 路由ID
     * @return 路由URL记录
     */
    RouterUrl getRouterUrlById(Integer id);

    /**
     * 新增路由URL记录
     *
     * @param routerUrl 路由URL对象
     * @return 影响行数
     */
    int insert(RouterUrl routerUrl);

    /**
     * 更新路由URL记录
     *
     * @param routerUrl 路由URL对象
     * @return 影响行数
     */
    int update(RouterUrl routerUrl);

    /**
     * 根据ID删除路由URL记录
     *
     * @param id 路由ID
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 获取所有路由信息（不包含创建时间）
     *
     * @return 路由列表
     */
    List<RouterUrl> listRouterUrlsWithoutCreatedAt();

    /**
     * 根据ID查询路由信息（不包含创建时间）
     *
     * @param id 路由ID
     * @return 路由信息
     */
    RouterUrl getRouterUrlByIdWithoutCreatedAt(Integer id);
}
