package com.david.hlp.Spring.repeater.mapper;

import org.apache.ibatis.annotations.Mapper;
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
    List<ProjectUrl> listProjects(@Param("limit") Integer limit, @Param("offset") Integer offset,
            @Param("projectName") String projectName, @Param("projectInterfaceName") String projectInterfaceName);

    /**
     * 根据ID获取项目信息
     *
     * @param id 项目ID
     * @return 项目信息
     */
    ProjectUrl getProjectById(@Param("id") Integer id);

    /**
     * 新增项目
     *
     * @param projectUrl 项目信息
     * @return 影响行数
     */
    int insertProject(ProjectUrl projectUrl);

    /**
     * 更新项目信息
     *
     * @param projectUrl 项目信息
     * @return 影响行数
     */
    int updateProject(ProjectUrl projectUrl);

    /**
     * 根据ID删除项目
     *
     * @param id 项目ID
     * @return 影响行数
     */
    int deleteProjectById(@Param("id") Integer id);

    /**
     * 检查项目名称是否存在
     *
     * @param projectName 项目名称
     * @return 存在返回1，不存在返回0
     */
    int checkProjectNameExists(@Param("projectName") String projectName);

    /**
     * 获取项目总数
     *
     * @param projectName 项目名称
     * @param projectInterfaceName 接口名称
     * @return 项目总数
     */
    long getProjectCount(@Param("projectName") String projectName, @Param("projectInterfaceName") String projectInterfaceName);

    /**
     * 获取所有项目基本信息列表
     *
     * @return 项目基本信息列表
     */
    List<ProjectUrl> listProjectBasicInfo();

    /**
     * 根据ID获取项目基本信息
     *
     * @param id 项目ID
     * @return 项目基本信息
     */
    ProjectUrl getProjectBasicInfoById(@Param("id") Integer id);

    /**
     * 根据ID获取项目详细信息
     *
     * @param id 项目ID
     * @return 项目详细信息
     */
    ProjectUrl getById(@Param("id") Integer id);
}