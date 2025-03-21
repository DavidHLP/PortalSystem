package com.david.hlp.SpringBootWork.repeater.mapper;

import com.david.hlp.SpringBootWork.repeater.entity.Url;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface PathUrlMapper {
    @Select("SELECT * FROM url WHERE id = #{id}")
    Url findById(Integer id);

    @Select("SELECT * FROM url")
    List<Url> findAll();

    @Select("<script>" +
            "SELECT * FROM url " +
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
    List<Url> findByConditions(@Param("routerId") Integer routerId,
                              @Param("hostId") Integer hostId,
                              @Param("portId") Integer portId,
                              @Param("projectId") Integer projectId,
                              @Param("method") String method,
                              @Param("isActive") Integer isActive,
                              @Param("protocol") String protocol,
                              @Param("offset") Integer offset,
                              @Param("limit") Integer limit);

    @Select("<script>" +
            "SELECT COUNT(*) FROM url " +
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

    @Insert("INSERT INTO url (protocol, host_id, port_id, router_id, project_id, method, is_active, description) " +
            "VALUES (#{protocol}, #{hostId}, #{portId}, #{routerId}, #{projectId}, #{method}, #{isActive}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Url url);

    @Update("UPDATE url SET protocol = #{protocol}, host_id = #{hostId}, port_id = #{portId}, " +
            "router_id = #{routerId}, project_id = #{projectId}, method = #{method}, " +
            "is_active = #{isActive}, description = #{description} WHERE id = #{id}")
    int update(Url url);

    @Delete("DELETE FROM url WHERE id = #{id}")
    int deleteById(Integer id);
}
