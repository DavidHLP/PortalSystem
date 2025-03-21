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
     * @param number 端口号（可选）
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 端口列表
     */
    @Select("<script>"
            + "SELECT * FROM port_url "
            + "<where>"
            + "<if test='number != null and number != \"\"'>"
            + "  number LIKE CONCAT('%', #{number}, '%') "
            + "</if>"
            + "</where>"
            + "ORDER BY id DESC "
            + "LIMIT #{offset}, #{limit}"
            + "</script>")
    List<PortUrl> selectPortList(@Param("number") String number, 
                                @Param("offset") int offset, 
                                @Param("limit") int limit);
    
    /**
     * 获取端口总数（带条件查询）
     * @param number 端口号（可选）
     * @return 符合条件的端口总数
     */
    @Select("<script>"
            + "SELECT COUNT(*) FROM port_url "
            + "<where>"
            + "<if test='number != null and number != \"\"'>"
            + "  number LIKE CONCAT('%', #{number}, '%') "
            + "</if>"
            + "</where>"
            + "</script>")
    int countPort(@Param("number") String number);
    
    /**
     * 根据ID获取端口信息
     * @param id 端口ID
     * @return 端口信息
     */
    @Select("SELECT * FROM port_url WHERE id = #{id}")
    PortUrl selectPortById(@Param("id") Integer id);
    
    /**
     * 根据端口号获取端口信息
     * @param number 端口号
     * @return 端口信息
     */
    @Select("SELECT * FROM port_url WHERE number = #{number}")
    PortUrl selectPortByNumber(@Param("number") String number);
    
    /**
     * 新增端口
     * @param port 端口信息
     * @return 影响行数
     */
    @Insert("INSERT INTO port_url(number, description) VALUES(#{number}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertPort(PortUrl port);
    
    /**
     * 更新端口信息
     * @param port 端口信息
     * @return 影响行数
     */
    @Update("UPDATE port_url SET number = #{number}, description = #{description} WHERE id = #{id}")
    int updatePort(PortUrl port);
    
    /**
     * 删除端口
     * @param id 端口ID
     * @return 影响行数
     */
    @Delete("DELETE FROM port_url WHERE id = #{id}")
    int deletePort(@Param("id") Integer id);

    @Select("SELECT * FROM port_url WHERE id = #{id}")
    PortUrl findById(Integer id);

    /**
     * 获取所有端口信息
     * 不包含描述
     * @return 端口列表
     */
    @Select("SELECT id, number FROM port_url")
    List<PortUrl> listAll();

    /**
     * 根据ID查询端口
     * 不包含描述
     */
    @Select("SELECT id, number FROM port_url WHERE id = #{id}")
    PortUrl findByIdHasNoDescription(@Param("id") Integer id);
}