package com.david.hlp.Spring.repeater.service;

import com.david.hlp.Spring.repeater.entity.UserUrl;
import com.david.hlp.Spring.common.baseinterface.*;

public interface UserUrlService extends BaseService<UserUrl, Integer> {
    UserUrl save(UserUrl userUrl);
}
