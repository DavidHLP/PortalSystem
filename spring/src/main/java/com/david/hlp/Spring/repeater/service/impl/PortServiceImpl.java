package com.david.hlp.Spring.repeater.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param number 端口号（可选）
     * @param page 页码
     * @param limit 每页数量
     * @return 端口列表和总数
     */
    @Override
    public PageInfo<PortUrl> getPortList(Integer limit, Integer offset, String number){
        offset = (offset - 1) * limit;

        List<PortUrl> portList = portMapper.listPorts(number, offset, limit);
        int total = portMapper.getPortCount(number);

        return PageInfo.<PortUrl>builder()
            .items(portList)
            .pageSize(limit)
            .total((long)total)
            .build();
    }

    /**
     * 根据ID获取端口详情
     *
     * @param id 端口ID
     * @return 端口详情
     */
    @Override
    public PortUrl getById(Long id) {
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
        // 检查端口号是否已存在
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
        // 检查端口是否存在
        PortUrl existPort = portMapper.getPortById(port.getId());
        if (existPort == null) {
            throw new BusinessException("端口不存在");
        }

        // 如果修改了端口号，需要检查新端口号是否已存在
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
        // 检查端口是否存在
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
    public PageInfo<PortUrl> listAll() {
        List<PortUrl> portList = portMapper.listAllPorts();
        return PageInfo.<PortUrl>builder()
            .items(portList)
            .pageSize(portList.size())
            .total((long)portList.size())
            .build();
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
     * @return 分页结果
     */
    @Override
    public PageInfo<PortUrl> getPage(Integer page, Integer limit) {
        return getPortList(limit, page, null);
    }
}