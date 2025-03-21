package com.david.hlp.Spring.repeater.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.david.hlp.Spring.repeater.entity.HostUrl;

import java.util.List;

@Mapper
public interface HostMapper extends BaseMapper<HostUrl> {
    /**
     * 分页查询主机列表
     * @param limit 每页记录数
     * @param offset 偏移量
     * @param address 主机地址
     * @param isActive 是否激活
     * @return 主机列表
     */
    @Select("""
            <script>
                SELECT id, address, description, is_active FROM host_url
                <where>
                    <if test='address != null'>address LIKE CONCAT('%', #{address}, '%')</if>
                    <if test='isActive != null'>AND is_active = #{isActive}</if>
                </where>
                <if test='limit != null'>LIMIT #{limit}</if>
                <if test='offset != null'>OFFSET #{offset}</if>
            </script>
            """)
    List<HostUrl> listHosts(@Param("limit") Integer limit, @Param("offset") Integer offset,
                          @Param("address") String address, @Param("isActive") Boolean isActive);

    /**
     * 获取所有主机基本信息
     * @return 主机列表
     */
    @Select("SELECT id, address, is_active FROM host_url")
    List<HostUrl> listAllHosts();

    /**
     * 根据ID获取主机信息
     * @param id 主机ID
     * @return 主机信息
     */
    @Select("SELECT id, address, description, is_active FROM host_url WHERE id = #{id}")
    HostUrl getHostById(@Param("id") Integer id);

    /**
     * 根据ID获取主机基本信息
     * @param id 主机ID
     * @return 主机基本信息
     */
    @Select("SELECT id, address, is_active FROM host_url WHERE id = #{id}")
    HostUrl getHostBasicInfoById(@Param("id") Integer id);

    /**
     * 新增主机
     * @param hostUrl 主机信息
     * @return 影响行数
     */
    @Insert("INSERT INTO host_url(address, description, is_active) VALUES(#{address}, #{description}, #{isActive})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertHost(HostUrl hostUrl);

    /**
     * 更新主机信息
     * @param hostUrl 主机信息
     * @return 影响行数
     */
    @Update("UPDATE host_url SET address = #{address}, description = #{description}, is_active = #{isActive} WHERE id = #{id}")
    int updateHost(HostUrl hostUrl);

    /**
     * 根据ID删除主机
     * @param id 主机ID
     * @return 影响行数
     */
    @Delete("DELETE FROM host_url WHERE id = #{id}")
    int deleteHostById(@Param("id") Integer id);

    /**
     * 检查主机地址是否存在
     * @param address 主机地址
     * @return 存在返回1，不存在返回0
     */
    @Select("SELECT COUNT(1) FROM host_url WHERE address = #{address}")
    int checkHostAddressExists(@Param("address") String address);

    /**
     * 获取主机总数
     * @param limit 每页记录数
     * @param offset 偏移量
     * @param address 主机地址
     * @param isActive 是否激活
     * @return 主机总数
     */
    @Select("""
            <script>
                SELECT COUNT(1) FROM host_url
                <where>
                    <if test='address != null'>address LIKE CONCAT('%', #{address}, '%')</if>
                    <if test='isActive != null'>AND is_active = #{isActive}</if>
                </where>
            </script>
            """)
    long getHostCount(@Param("limit") Integer limit, @Param("offset") Integer offset, 
                     @Param("address") String address, @Param("isActive") Boolean isActive);
}
