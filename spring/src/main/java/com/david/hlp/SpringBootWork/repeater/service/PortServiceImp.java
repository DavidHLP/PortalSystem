package com.david.hlp.SpringBootWork.repeater.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.david.hlp.SpringBootWork.repeater.mapper.PortMapper;
import com.david.hlp.SpringBootWork.repeater.entity.PortUrl;
import com.david.hlp.SpringBootWork.common.exception.BusinessException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PortServiceImp{

    private final PortMapper portMapper;
    
    /**
     * 获取端口列表
     * @param number 端口号（可选）
     * @param page 页码
     * @param limit 每页数量
     * @return 端口列表和总数
     */
    public Map<String, Object> getPortList(String number, int page, int limit) {
        int offset = (page - 1) * limit;
        
        List<PortUrl> portList = portMapper.selectPortList(number, offset, limit);
        int total = portMapper.countPort(number);
        
        Map<String, Object> result = new HashMap<>(2);
        result.put("items", portList);
        result.put("total", total);
        
        return result;
    }
    
    /**
     * 获取端口详情
     * @param id 端口ID
     * @return 端口详情
     */
    public PortUrl getPortById(Integer id) {
        PortUrl port = portMapper.selectPortById(id);
        if (port == null) {
            throw new BusinessException("端口不存在");
        }
        return port;
    }
    
    /**
     * 创建端口
     * @param port 端口信息
     * @return 创建后的端口信息
     */
    @Transactional
    public PortUrl createPort(PortUrl port) {
        // 检查端口号是否已存在
        PortUrl existPort = portMapper.selectPortByNumber(port.getNumber());
        if (existPort != null) {
            throw new BusinessException("端口号已存在");
        }
        
        portMapper.insertPort(port);
        return port;
    }
    
    /**
     * 更新端口
     * @param id 端口ID
     * @param port 端口信息
     */
    @Transactional
    public void updatePort(Integer id, PortUrl port) {
        // 检查端口是否存在
        PortUrl existPort = portMapper.selectPortById(id);
        if (existPort == null) {
            throw new BusinessException("端口不存在");
        }
        
        // 如果修改了端口号，需要检查新端口号是否已存在
        if (!existPort.getNumber().equals(port.getNumber())) {
            PortUrl portWithSameNumber = portMapper.selectPortByNumber(port.getNumber());
            if (portWithSameNumber != null) {
                throw new BusinessException("端口号已存在");
            }
        }
        
        port.setId(id);
        portMapper.updatePort(port);
    }
    
    /**
     * 删除端口
     * @param id 端口ID
     */
    @Transactional
    public void deletePort(Integer id) {
        // 检查端口是否存在
        PortUrl existPort = portMapper.selectPortById(id);
        if (existPort == null) {
            throw new BusinessException("端口不存在");
        }
        
        portMapper.deletePort(id);
    }

    public List<PortUrl> listAll() {
        return portMapper.listAll();
    }
}