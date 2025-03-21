package com.david.hlp.Spring.repeater.service.impl;

import org.springframework.stereotype.Service;
import com.david.hlp.Spring.repeater.mapper.HostMapper;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.repeater.entity.HostUrl;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.david.hlp.Spring.repeater.service.HostService;

/**
 * 主机服务实现类
 *
 * @author david
 * @date 2024/03/21
 */
@Service
@RequiredArgsConstructor
public class HostServiceImpl implements HostService {
    private final HostMapper hostMapper;

    @Override
    public HostUrl getById(Integer id) {
        return hostMapper.getHostById(id);
    }

    @Override
    public HostUrl create(HostUrl entity) {
        if (hostMapper.checkHostAddressExists(entity.getAddress()) > 0) {
            return null;
        }
        return hostMapper.insertHost(entity) > 0 ? entity : null;
    }

    @Override
    public HostUrl update(HostUrl entity) {
        HostUrl oldHost = hostMapper.getHostById(entity.getId());
        if (oldHost == null) {
            return null;
        }

        if (!oldHost.getAddress().equals(entity.getAddress())) {
            if (hostMapper.checkHostAddressExists(entity.getAddress()) > 0) {
                return null;
            }
        }

        return hostMapper.updateHost(entity) > 0 ? entity : null;
    }

    @Override
    public void deleteById(Integer id) {
        hostMapper.deleteHostById(id);
    }

    @Override
    public PageInfo<HostUrl> getPage(Integer page, Integer limit) {
        int offset = (page - 1) * limit;
        List<HostUrl> hosts = hostMapper.listHosts(limit, offset, null, null);
        long total = hostMapper.getHostCount(limit, offset, null, null);
        return PageInfo.<HostUrl>builder()
            .items(hosts)
            .pageNum(page)
            .pageSize(limit)
            .total(total)
            .build();
    }

    @Override
    public PageInfo<HostUrl> listAll() {
        List<HostUrl> hosts = hostMapper.listAllHosts();
        return PageInfo.<HostUrl>builder()
            .items(hosts)
            .pageNum(1)
            .pageSize(hosts.size())
            .total((long)hosts.size())
            .build();
    }

    /**
     * 更新主机状态
     *
     * @param id 主机ID
     * @param isActive 状态
     * @return 是否更新成功
     */
    public boolean updateHostStatus(Integer id, Boolean isActive) {
        HostUrl hostUrl = hostMapper.getHostById(id);
        if (hostUrl == null) {
            return false;
        }

        hostUrl.setIsActive(isActive);
        return hostMapper.updateHost(hostUrl) > 0;
    }

    @Override
    public PageInfo<HostUrl> getHostList(Integer limit, Integer offset, String address, Boolean isActive) {
        List<HostUrl> hosts = hostMapper.listHosts(limit, offset, address, isActive);
        long total = hostMapper.getHostCount(limit, offset, address, isActive);
        return PageInfo.<HostUrl>builder()
            .items(hosts)
            .total((long)total)
            .build();
    }
}
