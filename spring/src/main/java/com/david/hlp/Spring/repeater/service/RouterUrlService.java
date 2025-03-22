package com.david.hlp.Spring.repeater.service;

import com.david.hlp.Spring.repeater.entity.RouterUrl;
import java.util.List;
import com.david.hlp.Spring.common.baseinterface.BaseService;

/**
 * 路由URL服务接口
 */
public interface RouterUrlService extends BaseService<RouterUrl, Integer> {
    /**
     * 获取所有路由URL列表
     *
     * @return 路由URL列表
     */
    List<RouterUrl> listRouterUrls();
}
