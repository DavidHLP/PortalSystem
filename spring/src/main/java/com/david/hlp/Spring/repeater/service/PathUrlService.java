package com.david.hlp.Spring.repeater.service;

import com.david.hlp.Spring.common.interfaceI.BaseService;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.repeater.entity.ProjectUrlRequest;
import com.david.hlp.Spring.repeater.entity.Url;

import java.util.List;

/**
 * 路径URL服务接口
 */
public interface PathUrlService extends BaseService<Url, Integer> {
    /**
     * 查询项目URL请求列表
     * @param page 页码
     * @param limit 每页大小
     * @param routerId 路由ID
     * @param hostId 主机ID
     * @param portId 端口ID
     * @param projectId 项目ID
     * @param method 请求方法
     * @param isActive 是否激活
     * @param protocol 协议
     * @return 分页结果
     */
    PageInfo<ProjectUrlRequest> getProjectUrlRequestList(Integer page, Integer limit, Integer routerId, Integer hostId, Integer portId, Integer projectId, String method, Integer isActive, String protocol);
    /**
     * 获取分页结果
     * @param page 页码
     * @param limit 每页大小
     * @param entity 查询条件
     * @return 分页结果
     */
    PageInfo<ProjectUrlRequest> getPageUltra(Integer page, Integer limit, Url entity);

    /**
     * 根据项目ID获取URL列表
     * @param projectId 项目ID
     * @return URL列表
     */
    List<Url> getByProjectId(Integer projectId);

    /**
     * 根据URL ID获取URL列表
     * @param urlId 用户ID
     * @return URL列表
     */
    List<Url> getByListUrlId(List<Integer> urlIds);
}
