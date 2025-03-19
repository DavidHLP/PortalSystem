package com.david.hlp.SpringBootWork.repeater.service;
import org.springframework.stereotype.Service;

import com.david.hlp.SpringBootWork.repeater.mapper.HostMapper;
import com.david.hlp.SpringBootWork.common.result.PageInfo;
import com.david.hlp.SpringBootWork.repeater.entity.HostUrl;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HostServiceImp {
    private final HostMapper hostMapper;

    /**
     * 查询所有主机信息
     * @param limit 限制数量
     * @param offset 偏移量
     * @param address 地址过滤条件
     * @param isActive 状态过滤条件
     * @return 包含主机列表和总数的Map
     */
    public PageInfo<HostUrl> findAllWithCount(Integer limit, Integer offset, String address, Boolean isActive) {
        List<HostUrl> hosts = hostMapper.findAll(limit, offset, address, isActive);
        long total = hostMapper.countByLimitAndOffset(limit, offset, address, isActive);

        return PageInfo.<HostUrl>builder()
            .items(hosts)
            .total(total)
            .pageNum(offset)
            .pageSize(limit)
            .build();
    }

    /**
     * 根据ID查询主机
     * @param id 主机ID
     * @return 主机信息
     */
    public HostUrl findById(Integer id) {
        return hostMapper.findById(id);
    }

    /**
     * 新增主机
     * @param hostUrl 主机信息
     * @return 添加结果
     */
    public boolean create(HostUrl hostUrl) {
        // 检查地址是否已存在
        if (hostMapper.existsByAddress(hostUrl.getAddress()) > 0) {
            return false;
        }
        return hostMapper.insert(hostUrl) > 0;
    }

    /**
     * 更新主机
     * @param hostUrl 主机信息
     * @return 更新结果
     */
    public boolean update(HostUrl hostUrl) {
        // 获取旧数据
        HostUrl oldHost = hostMapper.findById(hostUrl.getId());
        if (oldHost == null) {
            return false;
        }

        // 如果地址变更了，则检查新地址是否已存在
        if (!oldHost.getAddress().equals(hostUrl.getAddress())) {
            if (hostMapper.existsByAddress(hostUrl.getAddress()) > 0) {
                return false;
            }
        }

        return hostMapper.update(hostUrl) > 0;
    }

    /**
     * 删除主机
     * @param id 主机ID
     * @return 删除结果
     */
    public boolean delete(Integer id) {
        return hostMapper.deleteById(id) > 0;
    }

    /**
     * 修改主机状态
     * @param id 主机ID
     * @param isActive 状态
     * @return 更新结果
     */
    public boolean updateStatus(Integer id, Boolean isActive) {
        HostUrl hostUrl = hostMapper.findById(id);
        if (hostUrl == null) {
            return false;
        }

        hostUrl.setIsActive(isActive);
        return hostMapper.update(hostUrl) > 0;
    }

    public List<HostUrl> listAll() {
        return hostMapper.listAll();
    }
}
