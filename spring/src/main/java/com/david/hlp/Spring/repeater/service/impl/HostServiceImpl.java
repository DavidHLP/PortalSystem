package com.david.hlp.Spring.repeater.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.david.hlp.Spring.repeater.mapper.HostMapper;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.repeater.entity.HostUrl;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.david.hlp.Spring.repeater.service.HostService;
import com.david.hlp.Spring.common.exception.BusinessException;
import com.david.hlp.Spring.common.enums.ResultCode;

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
        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "主机ID不能为空");
        }
        return hostMapper.getHostById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HostUrl create(HostUrl entity) {
        if (entity == null || entity.getAddress() == null || entity.getAddress().trim().isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "主机地址不能为空");
        }

        if (hostMapper.checkHostAddressExists(entity.getAddress(), null) > 0) {
            throw new BusinessException(ResultCode.DUPLICATE_ERROR, "主机地址已存在");
        }

        return hostMapper.insertHost(entity) > 0 ? entity : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HostUrl update(HostUrl entity) {
        if (entity == null || entity.getId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "主机信息不完整");
        }

        HostUrl oldHost = hostMapper.getHostById(entity.getId());
        if (oldHost == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "主机不存在");
        }

        if (!oldHost.getAddress().equals(entity.getAddress())) {
            if (hostMapper.checkHostAddressExists(entity.getAddress(), entity.getId()) > 0) {
                throw new BusinessException(ResultCode.DUPLICATE_ERROR, "主机地址已存在");
            }
        }

        return hostMapper.updateHost(entity) > 0 ? entity : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "主机ID不能为空");
        }
        hostMapper.deleteHostById(id);
    }

    @Override
    public PageInfo<HostUrl> getPage(Integer page, Integer limit, HostUrl entity) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (limit == null || limit < 1) {
            limit = 10;
        }
        int offset = (page - 1) * limit;
        List<HostUrl> hosts = hostMapper.listHosts(limit, offset, entity.getAddress(), entity.getIsActive());
        long total = hostMapper.getHostCount(limit, offset, entity.getAddress(), entity.getIsActive());
        return PageInfo.<HostUrl>builder()
            .items(hosts)
            .pageNum(page)
            .pageSize(limit)
            .total(total)
            .build();
    }

    @Override
    public List<HostUrl> listAll() {
        return hostMapper.listAllHosts();
    }

    /**
     * 更新主机状态
     *
     * @param id 主机ID
     * @param isActive 状态
     * @return 是否更新成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateHostStatus(Integer id, Boolean isActive) {
        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "主机ID不能为空");
        }
        if (isActive == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "状态不能为空");
        }

        HostUrl hostUrl = hostMapper.getHostById(id);
        if (hostUrl == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "主机不存在");
        }

        hostUrl.setIsActive(isActive);
        return hostMapper.updateHost(hostUrl) > 0;
    }

    @Override
    public PageInfo<HostUrl> getHostList(Integer limit, Integer offset, String address, Boolean isActive) {
        if (limit == null || limit < 1) {
            limit = 10;
        }
        if (offset == null || offset < 0) {
            offset = 0;
        }

        List<HostUrl> hosts = hostMapper.listHosts(limit, offset, address, isActive);
        long total = hostMapper.getHostCount(limit, offset, address, isActive);
        return PageInfo.<HostUrl>builder()
            .items(hosts)
            .total(total)
            .build();
    }
}
