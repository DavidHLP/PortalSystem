package com.david.hlp.Spring.repeater.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.david.hlp.Spring.repeater.mapper.PortMapper;
import com.david.hlp.Spring.repeater.entity.PortUrl;
import com.david.hlp.Spring.common.exception.BusinessException;
import com.david.hlp.Spring.repeater.service.PortService;
import java.util.List;
import com.david.hlp.Spring.common.result.PageInfo;
import lombok.RequiredArgsConstructor;

/**
 * 端口服务实现类
 *
 * @author david
 * @date 2024/03/21
 */
@Service
@RequiredArgsConstructor
public class PortServiceImpl implements PortService {

    private final PortMapper portMapper;

    /**
     * 获取端口列表
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param number 端口号（可选）
     * @return 端口列表和总数
     */
    @Override
    public PageInfo<PortUrl> getPortList(Integer pageNum, Integer pageSize, String number) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        PortUrl query = new PortUrl();
        query.setNumber(number);
        return getPage(pageNum, pageSize, query);
    }

    /**
     * 根据ID获取端口详情
     *
     * @param id 端口ID
     * @return 端口详情
     */
    @Override
    public PortUrl getById(Long id) {
        if (id == null) {
            throw new BusinessException("端口ID不能为空");
        }
        PortUrl port = portMapper.getPortById(id);
        if (port == null) {
            throw new BusinessException("端口不存在");
        }
        return port;
    }

    /**
     * 根据ID获取端口详情（业务方法）
     *
     * @param id 端口ID
     * @return 端口详情
     */
    @Override
    public PortUrl getPortById(Long id) {
        return getById(id);
    }

    /**
     * 创建端口
     *
     * @param port 端口信息
     * @return 创建后的端口信息
     */
    @Override
    public PortUrl create(PortUrl port) {
        if (port == null) {
            throw new BusinessException("端口信息不能为空");
        }
        if (!StringUtils.hasText(port.getNumber())) {
            throw new BusinessException("端口号不能为空");
        }
        
        PortUrl existPort = portMapper.getPortByNumber(port.getNumber());
        if (existPort != null) {
            throw new BusinessException("端口号已存在");
        }

        portMapper.insertPort(port);
        return port;
    }

    /**
     * 创建端口（业务方法）
     *
     * @param port 端口信息
     * @return 创建后的端口信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public PortUrl createPort(PortUrl port) {
        return create(port);
    }

    /**
     * 更新端口
     *
     * @param port 端口信息
     * @return 更新后的端口信息
     */
    @Override
    public PortUrl update(PortUrl port) {
        if (port == null || port.getId() == null) {
            throw new BusinessException("端口信息或ID不能为空");
        }
        if (!StringUtils.hasText(port.getNumber())) {
            throw new BusinessException("端口号不能为空");
        }

        PortUrl existPort = portMapper.getPortById(port.getId());
        if (existPort == null) {
            throw new BusinessException("端口不存在");
        }

        if (!existPort.getNumber().equals(port.getNumber())) {
            PortUrl portWithSameNumber = portMapper.getPortByNumber(port.getNumber());
            if (portWithSameNumber != null) {
                throw new BusinessException("端口号已存在");
            }
        }

        portMapper.updatePort(port);
        return port;
    }

    /**
     * 更新端口（业务方法）
     *
     * @param id 端口ID
     * @param port 端口信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePort(Long id, PortUrl port) {
        if (id == null) {
            throw new BusinessException("端口ID不能为空");
        }
        port.setId(id);
        update(port);
    }

    /**
     * 删除端口
     *
     * @param id 端口ID
     */
    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new BusinessException("端口ID不能为空");
        }
        PortUrl existPort = portMapper.getPortById(id);
        if (existPort == null) {
            throw new BusinessException("端口不存在");
        }
        portMapper.deletePort(id);
    }

    /**
     * 删除端口（业务方法）
     *
     * @param id 端口ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePort(Long id) {
        deleteById(id);
    }

    /**
     * 获取所有端口列表
     *
     * @return 端口列表
     */
    @Override
    public List<PortUrl> listAll() {
        return portMapper.listAllPorts();
    }

    /**
     * 获取所有端口列表（业务方法）
     *
     * @return 端口列表
     */
    @Override
    public List<PortUrl> listAllPorts() {
        return portMapper.listAllPorts();
    }

    /**
     * 分页查询端口列表
     *
     * @param page 页码
     * @param limit 每页数量
     * @param entity 查询条件
     * @return 分页结果
     */
    @Override
    public PageInfo<PortUrl> getPage(Integer page, Integer limit, PortUrl entity) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (limit == null || limit < 1) {
            limit = 10;
        }
        
        int offset = (page - 1) * limit;
        String number = entity != null ? entity.getNumber() : null;
        
        List<PortUrl> portList = portMapper.listPorts(number, offset, limit);
        int total = portMapper.getPortCount(number);
        int pages = (total + limit - 1) / limit;
        
        return PageInfo.<PortUrl>builder()
                .items(portList)
                .pageNum(page)
                .pageSize(limit)
                .total((long) total)
                .pages(pages)
                .hasNext(page < pages)
                .hasPrevious(page > 1)
                .build();
    }
}