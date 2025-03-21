package com.david.hlp.Spring.repeater.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.david.hlp.Spring.repeater.entity.ProjectUrl;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface ProjectMapper extends BaseMapper<ProjectUrl> {
    /**
     * 查询所有项目
     */
    @Select("""
            <script>
                SELECT * FROM project_url
                <where>
                    <if test='projectName != null'>project_name LIKE CONCAT('%', #{projectName}, '%')</if>
                    <if test='projectInterfaceName != null'>AND project_interface_name LIKE CONCAT('%', #{projectInterfaceName}, '%')</if>
                </where>
                <if test='limit != null'>LIMIT #{limit}</if>
                <if test='offset != null'>OFFSET #{offset}</if>
            </script>
            """)
    List<ProjectUrl> findAll(@Param("limit") Integer limit, @Param("offset") Integer offset, @Param("projectName") String projectName, @Param("projectInterfaceName") String projectInterfaceName);

    /**
     * 根据ID查询项目
     */
    @Select("SELECT * FROM project_url WHERE id = #{id}")
    ProjectUrl findById(@Param("id") Integer id);

    /**
     * 插入新项目
     */
    @Insert("INSERT INTO project_url(project_name, project_interface_name, description) " +
            "VALUES(#{projectName}, #{projectInterfaceName}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ProjectUrl projectUrl);

    /**
     * 更新项目信息
     */
    @Update("UPDATE project_url SET project_name = #{projectName}, " +
            "project_interface_name = #{projectInterfaceName}, " +
            "description = #{description} " +
            "WHERE id = #{id}")
    int update(ProjectUrl projectUrl);

    /**
     * 根据ID删除项目
     */
    @Delete("DELETE FROM project_url WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);

    /**
     * 检查项目名称是否存在
     */
    @Select("SELECT COUNT(1) FROM project_url WHERE project_name = #{projectName}")
    int existsByProjectName(@Param("projectName") String projectName);

    /**
     * 获取项目总数
     */
    @Select("""
            <script>
                SELECT COUNT(1) FROM project_url
                <where>
                    <if test='projectName != null'>project_name LIKE CONCAT('%', #{projectName}, '%')</if>
                    <if test='projectInterfaceName != null'>AND project_interface_name LIKE CONCAT('%', #{projectInterfaceName}, '%')</if>
                </where>
            </script>
            """)
    long countByLimitAndOffset(@Param("limit") Integer limit, @Param("offset") Integer offset, @Param("projectName") String projectName, @Param("projectInterfaceName") String projectInterfaceName);

    /**
     * 获取所有项目信息
     * 不包含解释文档
     * @return 项目列表
     */
    @Select("SELECT id, project_name, project_interface_name FROM project_url")
    List<ProjectUrl> listAll();

    /**
     * 根据ID查询项目
     * 不包含解释文档
     */
    @Select("SELECT id, project_name, project_interface_name FROM project_url WHERE id = #{id}")
    ProjectUrl findByIdHasNoDescription(@Param("id") Integer id);
}