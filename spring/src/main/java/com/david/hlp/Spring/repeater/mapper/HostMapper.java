package com.david.hlp.Spring.repeater.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.david.hlp.Spring.repeater.entity.HostUrl;

import java.util.List;

/**
 * 主机管理Mapper接口
 *
 * @author david
 * @date 2024/03/21
 */
@Mapper
public interface HostMapper extends BaseMapper<HostUrl> {
    /**
     * 分页查询主机列表
     *
     * @param limit 每页记录数
     * @param offset 偏移量
     * @param address 主机地址(模糊查询)
     * @param isActive 是否激活
     * @return 主机列表
     */
    List<HostUrl> listHosts(@Param("limit") Integer limit,
                          @Param("offset") Integer offset,
                          @Param("address") String address,
                          @Param("isActive") Boolean isActive);

    /**
     * 获取所有激活状态的主机基本信息
     *
     * @return 主机列表
     */
    List<HostUrl> listAllHosts();

    /**
     * 根据ID获取主机完整信息
     *
     * @param id 主机ID
     * @return 主机信息
     */
    HostUrl getHostById(@Param("id") Integer id);

    /**
     * 根据ID获取主机基本信息
     *
     * @param id 主机ID
     * @return 主机基本信息
     */
    HostUrl getHostBasicInfoById(@Param("id") Integer id);

    /**
     * 新增主机
     *
     * @param hostUrl 主机信息
     * @return 影响行数
     */
    int insertHost(HostUrl hostUrl);

    /**
     * 更新主机信息
     *
     * @param hostUrl 主机信息
     * @return 影响行数
     */
    int updateHost(HostUrl hostUrl);

    /**
     * 根据ID删除主机
     *
     * @param id 主机ID
     * @return 影响行数
     */
    int deleteHostById(@Param("id") Integer id);

    /**
     * 检查主机地址是否存在
     *
     * @param address 主机地址
     * @param id 排除的主机ID(更新时使用)
     * @return 存在返回1，不存在返回0
     */
    int checkHostAddressExists(@Param("address") String address, @Param("id") Integer id);

    /**
     * 获取主机总数
     *
     * @param limit 每页记录数
     * @param offset 偏移量
     * @param address 主机地址(模糊查询)
     * @param isActive 是否激活
     * @return 主机总数
     */
    long getHostCount(@Param("limit") Integer limit, 
                     @Param("offset") Integer offset, 
                     @Param("address") String address, 
                     @Param("isActive") Boolean isActive);
}
