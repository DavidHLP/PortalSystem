package com.david.hlp.Spring.repeater.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    List<PortUrl> listPorts(@Param("portNumber") String portNumber,
                           @Param("offset") int offset,
                           @Param("limit") int limit);

    /**
     * 获取端口总数（带条件查询）
     * @param portNumber 端口号（可选）
     * @return 符合条件的端口总数
     */
    int getPortCount(@Param("portNumber") String portNumber);

    /**
     * 根据ID获取端口信息
     * @param portId 端口ID
     * @return 端口信息
     */
    PortUrl getPortById(@Param("portId") Long portId);

    /**
     * 根据端口号获取端口信息
     * @param portNumber 端口号
     * @return 端口信息
     */
    PortUrl getPortByNumber(@Param("portNumber") String portNumber);

    /**
     * 新增端口
     * @param portUrl 端口信息
     * @return 影响行数
     */
    int insertPort(PortUrl portUrl);

    /**
     * 更新端口信息
     * @param portUrl 端口信息
     * @return 影响行数
     */
    int updatePort(PortUrl portUrl);

    /**
     * 删除端口
     * @param portId 端口ID
     * @return 影响行数
     */
    int deletePort(@Param("portId") Long portId);

    /**
     * 根据ID获取端口信息（包含所有字段）
     * @param portId 端口ID
     * @return 端口信息
     */
    PortUrl getPortByIdWithAllFields(@Param("portId") Integer portId);

    /**
     * 获取所有端口信息（仅包含ID和端口号）
     * @return 端口列表
     */
    List<PortUrl> listAllPorts();

    /**
     * 根据ID获取端口信息（仅包含ID和端口号）
     * @param portId 端口ID
     * @return 端口信息
     */
    PortUrl getPortByIdWithoutDescription(@Param("portId") Integer portId);
}