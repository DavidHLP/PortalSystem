package com.david.hlp.Spring.repeater.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;

import java.util.List;

import com.david.hlp.Spring.repeater.entity.PortUrl;

@Mapper
public interface PortMapper {
    /**
     * 获取端口列表（带条件查询）
     * @param portNumber 端口号（可选）
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 端口列表
     */
    @Select("<script>"
            + "SELECT * FROM port_url "
            + "<where>"
            + "<if test='portNumber != null and portNumber != \"\"'>"
            + "  number LIKE CONCAT('%', #{portNumber}, '%') "
            + "</if>"
            + "</where>"
            + "ORDER BY id DESC "
            + "LIMIT #{offset}, #{limit}"
            + "</script>")
    List<PortUrl> listPorts(@Param("portNumber") String portNumber,
                           @Param("offset") int offset,
                           @Param("limit") int limit);

    /**
     * 获取端口总数（带条件查询）
     * @param portNumber 端口号（可选）
     * @return 符合条件的端口总数
     */
    @Select("<script>"
            + "SELECT COUNT(*) FROM port_url "
            + "<where>"
            + "<if test='portNumber != null and portNumber != \"\"'>"
            + "  number LIKE CONCAT('%', #{portNumber}, '%') "
            + "</if>"
            + "</where>"
            + "</script>")
    int getPortCount(@Param("portNumber") String portNumber);

    /**
     * 根据ID获取端口信息
     * @param portId 端口ID
     * @return 端口信息
     */
    @Select("SELECT * FROM port_url WHERE id = #{portId}")
    PortUrl getPortById(@Param("portId") Long portId);

    /**
     * 根据端口号获取端口信息
     * @param portNumber 端口号
     * @return 端口信息
     */
    @Select("SELECT * FROM port_url WHERE number = #{portNumber}")
    PortUrl getPortByNumber(@Param("portNumber") String portNumber);

    /**
     * 新增端口
     * @param portUrl 端口信息
     * @return 影响行数
     */
    @Insert("INSERT INTO port_url(number, description) VALUES(#{number}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertPort(PortUrl portUrl);

    /**
     * 更新端口信息
     * @param portUrl 端口信息
     * @return 影响行数
     */
    @Update("UPDATE port_url SET number = #{number}, description = #{description} WHERE id = #{id}")
    int updatePort(PortUrl portUrl);

    /**
     * 删除端口
     * @param portId 端口ID
     * @return 影响行数
     */
    @Delete("DELETE FROM port_url WHERE id = #{portId}")
    int deletePort(@Param("portId") Long portId);

    /**
     * 根据ID获取端口信息（包含所有字段）
     * @param portId 端口ID
     * @return 端口信息
     */
    @Select("SELECT * FROM port_url WHERE id = #{portId}")
    PortUrl getPortByIdWithAllFields(@Param("portId") Integer portId);

    /**
     * 获取所有端口信息（仅包含ID和端口号）
     * @return 端口列表
     */
    @Select("SELECT id, number FROM port_url")
    List<PortUrl> listAllPorts();

    /**
     * 根据ID获取端口信息（仅包含ID和端口号）
     * @param portId 端口ID
     * @return 端口信息
     */
    @Select("SELECT id, number FROM port_url WHERE id = #{portId}")
    PortUrl getPortByIdWithoutDescription(@Param("portId") Integer portId);
}