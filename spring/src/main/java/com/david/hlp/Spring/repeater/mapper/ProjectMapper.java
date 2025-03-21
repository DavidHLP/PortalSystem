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

/**
 * 项目URL映射接口
 *
 * @author david
 */
@Mapper
public interface ProjectMapper extends BaseMapper<ProjectUrl> {
    
    /**
     * 获取项目列表
     *
     * @param limit 限制条数
     * @param offset 偏移量
     * @param projectName 项目名称
     * @param projectInterfaceName 接口名称
     * @return 项目列表
     */
    @Select("""
            <script>
                SELECT id, project_name, project_interface_name, description 
                FROM project_url
                <where>
                    <if test='projectName != null'>project_name LIKE CONCAT('%', #{projectName}, '%')</if>
                    <if test='projectInterfaceName != null'>AND project_interface_name LIKE CONCAT('%', #{projectInterfaceName}, '%')</if>
                </where>
                <if test='limit != null'>LIMIT #{limit}</if>
                <if test='offset != null'>OFFSET #{offset}</if>
            </script>
            """)
    List<ProjectUrl> listProjects(@Param("limit") Integer limit, @Param("offset") Integer offset, 
            @Param("projectName") String projectName, @Param("projectInterfaceName") String projectInterfaceName);

    /**
     * 根据ID获取项目信息
     *
     * @param id 项目ID
     * @return 项目信息
     */
    @Select("SELECT id, project_name, project_interface_name, description FROM project_url WHERE id = #{id}")
    ProjectUrl getProjectById(@Param("id") Integer id);

    /**
     * 新增项目
     *
     * @param projectUrl 项目信息
     * @return 影响行数
     */
    @Insert("INSERT INTO project_url(project_name, project_interface_name, description) " +
            "VALUES(#{projectName}, #{projectInterfaceName}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertProject(ProjectUrl projectUrl);

    /**
     * 更新项目信息
     *
     * @param projectUrl 项目信息
     * @return 影响行数
     */
    @Update("UPDATE project_url SET project_name = #{projectName}, " +
            "project_interface_name = #{projectInterfaceName}, " +
            "description = #{description} " +
            "WHERE id = #{id}")
    int updateProject(ProjectUrl projectUrl);

    /**
     * 根据ID删除项目
     *
     * @param id 项目ID
     * @return 影响行数
     */
    @Delete("DELETE FROM project_url WHERE id = #{id}")
    int deleteProjectById(@Param("id") Integer id);

    /**
     * 检查项目名称是否存在
     *
     * @param projectName 项目名称
     * @return 存在返回1，不存在返回0
     */
    @Select("SELECT COUNT(1) FROM project_url WHERE project_name = #{projectName}")
    int checkProjectNameExists(@Param("projectName") String projectName);

    /**
     * 获取项目总数
     *
     * @param projectName 项目名称
     * @param projectInterfaceName 接口名称
     * @return 项目总数
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
    long getProjectCount(@Param("projectName") String projectName, @Param("projectInterfaceName") String projectInterfaceName);

    /**
     * 获取所有项目基本信息列表
     *
     * @return 项目基本信息列表
     */
    @Select("SELECT id, project_name, project_interface_name FROM project_url")
    List<ProjectUrl> listProjectBasicInfo();

    /**
     * 根据ID获取项目基本信息
     *
     * @param id 项目ID
     * @return 项目基本信息
     */
    @Select("SELECT id, project_name, project_interface_name FROM project_url WHERE id = #{id}")
    ProjectUrl getProjectBasicInfoById(@Param("id") Integer id);
}