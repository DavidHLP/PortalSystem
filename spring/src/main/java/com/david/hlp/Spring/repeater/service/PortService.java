package com.david.hlp.Spring.repeater.service;

import com.david.hlp.Spring.repeater.entity.PortUrl;
import java.util.List;

import com.david.hlp.Spring.common.interfaceI.BaseService;
import com.david.hlp.Spring.common.result.PageInfo;

/**
 * 端口服务接口
 */
public interface PortService extends BaseService<PortUrl, Long> {
    /**
     * 根据条件查询端口列表
     * @param limit 每页大小
     * @param offset 偏移量
     * @param number 端口号
     * @return 分页结果
     */
    PageInfo<PortUrl> getPortList(Integer limit, Integer offset, String number);

    PortUrl getPortById(Long id);
    PortUrl createPort(PortUrl port);
    void updatePort(Long id, PortUrl port);
    void deletePort(Long id);
    List<PortUrl> listAllPorts();
}