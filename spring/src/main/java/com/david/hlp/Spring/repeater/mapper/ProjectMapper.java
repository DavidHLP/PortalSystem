package com.david.hlp.Spring.repeater.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.david.hlp.Spring.repeater.module.entity.Project;
import java.util.List;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProjectMapper {

    /**
     * 插入项目信息
     * @param project 项目信息实体
     * @return 插入结果
     */
    int insertProject(Project project);

    /**
     * 分页查询项目列表
     * @param project 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 项目列表
     */
    List<Project> listProject(@Param("project") Project project, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    /**
     * 查询项目总数
     * @param project 查询条件
     * @return 总数
     */
    Long countProject(@Param("project") Project project);

    /**
     * 更新项目信息
     * @param project 项目信息实体
     * @return 更新结果
     */
    int updateProject(Project project);

    /**
     * 删除项目信息
     * @param project 项目信息实体
     * @return 删除结果
     */
    int deleteProject(Project project);

    /**
     * 查询所有项目信息
     * @return 所有项目信息
     */
    List<Project> listAllProject();
}
