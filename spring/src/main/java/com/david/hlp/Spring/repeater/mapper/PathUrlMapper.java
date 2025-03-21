package com.david.hlp.Spring.repeater.mapper;

import com.david.hlp.Spring.repeater.entity.Url;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * URL路径映射Mapper
 *
 * @author david
 */
@Mapper
public interface PathUrlMapper {
    /**
     * 根据ID获取URL信息
     *
     * @param id URL ID
     * @return URL信息
     */
    @Select("SELECT id, protocol, host_id, port_id, router_id, project_id, method, is_active, description FROM url WHERE id = #{id}")
    Url getById(Integer id);

    /**
     * 获取所有URL信息
     *
     * @return URL列表
     */
    @Select("SELECT id, protocol, host_id, port_id, router_id, project_id, method, is_active, description FROM url")
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
    @Select("<script>" +
            "SELECT id, protocol, host_id, port_id, router_id, project_id, method, is_active, description FROM url " +
            "<where>" +
            "<if test='routerId != null'> AND router_id = #{routerId}</if>" +
            "<if test='hostId != null'> AND host_id = #{hostId}</if>" +
            "<if test='portId != null'> AND port_id = #{portId}</if>" +
            "<if test='projectId != null'> AND project_id = #{projectId}</if>" +
            "<if test='method != null and method != \"\"'> AND method = #{method}</if>" +
            "<if test='isActive != null'> AND is_active = #{isActive}</if>" +
            "<if test='protocol != null and protocol != \"\"'> AND protocol = #{protocol}</if>" +
            "</where>" +
            " LIMIT #{offset}, #{limit}" +
            "</script>")
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
    @Select("<script>" +
            "SELECT COUNT(id) FROM url " +
            "<where>" +
            "<if test='routerId != null'> AND router_id = #{routerId}</if>" +
            "<if test='hostId != null'> AND host_id = #{hostId}</if>" +
            "<if test='portId != null'> AND port_id = #{portId}</if>" +
            "<if test='projectId != null'> AND project_id = #{projectId}</if>" +
            "<if test='method != null and method != \"\"'> AND method = #{method}</if>" +
            "<if test='isActive != null'> AND is_active = #{isActive}</if>" +
            "<if test='protocol != null and protocol != \"\"'> AND protocol = #{protocol}</if>" +
            "</where>" +
            "</script>")
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
    @Insert("INSERT INTO url (protocol, host_id, port_id, router_id, project_id, method, is_active, description) " +
            "VALUES (#{protocol}, #{hostId}, #{portId}, #{routerId}, #{projectId}, #{method}, #{isActive}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Url url);

    /**
     * 更新URL信息
     *
     * @param url URL信息
     * @return 影响行数
     */
    @Update("UPDATE url SET protocol = #{protocol}, host_id = #{hostId}, port_id = #{portId}, " +
            "router_id = #{routerId}, project_id = #{projectId}, method = #{method}, " +
            "is_active = #{isActive}, description = #{description} WHERE id = #{id}")
    int update(Url url);

    /**
     * 根据ID删除URL信息
     *
     * @param id URL ID
     * @return 影响行数
     */
    @Delete("DELETE FROM url WHERE id = #{id}")
    int deleteById(Integer id);
}