package com.david.hlp.SpringBootWork.repeater.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.david.hlp.SpringBootWork.repeater.entity.HostUrl;

import java.util.List;

@Mapper
public interface HostMapper extends BaseMapper<HostUrl> {
    /**
     * 查询所有主机
     */
    @Select("""
            <script>
                SELECT * FROM host_url
                <where>
                    <if test='address != null'>address LIKE CONCAT('%', #{address}, '%')</if>
                    <if test='isActive != null'>AND is_active = #{isActive}</if>
                </where>
                <if test='limit != null'>LIMIT #{limit}</if>
                <if test='offset != null'>OFFSET #{offset}</if>
            </script>
            """)
    List<HostUrl> findAll(@Param("limit") Integer limit, @Param("offset") Integer offset,
                          @Param("address") String address, @Param("isActive") Boolean isActive);

    /**
     * 获取所有主机信息
     * 不包含描述
     * @return 主机列表
     */
    @Select("SELECT id, address, is_active FROM host_url")
    List<HostUrl> listAll();
    /**
     * 根据ID查询主机
     */
    @Select("SELECT * FROM host_url WHERE id = #{id}")
    HostUrl findById(@Param("id") Integer id);

    /**
     * 根据ID查询主机
     * 不包含描述
     */
    @Select("SELECT id, address, is_active FROM host_url WHERE id = #{id}")
    HostUrl findByIdHasNoDescription(@Param("id") Integer id);

    /**
     * 插入新主机
     */
    @Insert("INSERT INTO host_url(address, description, is_active) " +
            "VALUES(#{address}, #{description}, #{isActive})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(HostUrl hostUrl);

    /**
     * 更新主机信息
     */
    @Update("UPDATE host_url SET address = #{address}, " +
            "description = #{description}, " +
            "is_active = #{isActive} " +
            "WHERE id = #{id}")
    int update(HostUrl hostUrl);

    /**
     * 根据ID删除主机
     */
    @Delete("DELETE FROM host_url WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);

    /**
     * 检查地址是否存在
     */
    @Select("SELECT COUNT(1) FROM host_url WHERE address = #{address}")
    int existsByAddress(@Param("address") String address);

    /**
     * 获取主机总数
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
    long countByLimitAndOffset(@Param("limit") Integer limit, @Param("offset") Integer offset, 
                               @Param("address") String address, @Param("isActive") Boolean isActive);
}
