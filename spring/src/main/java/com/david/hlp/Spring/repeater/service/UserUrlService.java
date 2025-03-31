package com.david.hlp.Spring.repeater.service;

import com.david.hlp.Spring.common.interfaceI.*;
import com.david.hlp.Spring.repeater.entity.UserUrl;

public interface UserUrlService extends BaseService<UserUrl, Integer> {
    UserUrl save(UserUrl userUrl);
}
