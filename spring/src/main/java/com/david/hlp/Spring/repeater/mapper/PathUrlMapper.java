package com.david.hlp.Spring.repeater.mapper;

import com.david.hlp.Spring.repeater.entity.Url;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * URL路径映射Mapper
 *
 * @author david
 * @date 2024/03/21
 */
@Mapper
public interface PathUrlMapper {
    /**
     * 根据ID获取URL信息
     *
     * @param id URL ID
     * @return URL信息
     */
    Url getById(@Param("id") Integer id);

    /**
     * 获取所有URL信息列表
     *
     * @return URL列表
     */
    List<Url> listAll();

    /**
     * 根据条件分页查询URL列表
     *
     * @param routerId 路由ID
     * @param hostId 主机ID
     * @param portId 端口ID
     * @param projectId 项目ID
     * @param method 请求方法
     * @param isActive 是否激活
     * @param protocol 协议
     * @param offset 偏移量
     * @param limit 限制条数
     * @return URL列表
     */
    List<Url> listByConditions(@Param("routerId") Integer routerId,
                              @Param("hostId") Integer hostId,
                              @Param("portId") Integer portId,
                              @Param("projectId") Integer projectId,
                              @Param("method") String method,
                              @Param("isActive") Integer isActive,
                              @Param("protocol") String protocol,
                              @Param("offset") Integer offset,
                              @Param("limit") Integer limit);

    /**
     * 根据条件统计URL总数
     *
     * @param routerId 路由ID
     * @param hostId 主机ID
     * @param portId 端口ID
     * @param projectId 项目ID
     * @param method 请求方法
     * @param isActive 是否激活
     * @param protocol 协议
     * @return URL总数
     */
    long countByConditions(@Param("routerId") Integer routerId,
                          @Param("hostId") Integer hostId,
                          @Param("portId") Integer portId,
                          @Param("projectId") Integer projectId,
                          @Param("method") String method,
                          @Param("isActive") Integer isActive,
                          @Param("protocol") String protocol);

    /**
     * 新增URL信息
     *
     * @param url URL信息
     * @return 影响行数
     */
    int insert(Url url);

    /**
     * 更新URL信息
     *
     * @param url URL信息
     * @return 影响行数
     */
    int update(Url url);

    /**
     * 根据ID删除URL信息
     *
     * @param id URL ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 根据项目ID获取URL列表
     *
     * @param projectId 项目ID
     * @return URL列表
     */
    List<Url> getByProjectId(@Param("projectId") Integer projectId);

    /**
     * 根据用户ID获取URL列表
     *
     * @param urlId 用户ID
     * @return URL列表
     */
    Url getByUrlId(@Param("urlId") Integer urlId);
}