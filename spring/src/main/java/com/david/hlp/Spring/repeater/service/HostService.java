package com.david.hlp.Spring.repeater.service;

import com.david.hlp.Spring.repeater.entity.HostUrl;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.common.baseinterface.BaseService;
/**
 * 主机服务接口
 */
public interface HostService extends BaseService<HostUrl, Integer> {
    /**
     * 更新主机状态
     * @param id 主机ID
     * @param isActive 是否激活
     * @return 更新结果
     */
    boolean updateHostStatus(Integer id, Boolean isActive);

    /**
     * 根据条件查询主机列表
     * @param limit 每页大小
     * @param offset 偏移量
     * @param address 地址
     * @param isActive 是否激活
     * @return 分页结果
     */
    PageInfo<HostUrl> getHostList(Integer limit, Integer offset, String address, Boolean isActive);
}