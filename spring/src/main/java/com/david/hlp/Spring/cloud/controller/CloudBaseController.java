package com.david.hlp.Spring.cloud.controller;

import com.david.hlp.Spring.common.util.UserContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 云服务基础控制器
 * 提供通用的控制器功能
 */
@Slf4j
public class CloudBaseController {

    /**
     * 获取当前请求用户的邮箱
     *
     * @return 当前用户邮箱
     */
    protected String getCurrentUserEmail() {
        return UserContext.getCurrentEmail();
    }
}
