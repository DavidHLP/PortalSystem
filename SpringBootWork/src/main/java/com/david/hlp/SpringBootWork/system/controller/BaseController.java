package com.david.hlp.SpringBootWork.system.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.david.hlp.SpringBootWork.system.entity.auth.AuthUser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseController {

    protected Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            log.debug("Authentication: {}", authentication);
            AuthUser user = (AuthUser) authentication.getPrincipal();
            log.debug("Current User ID: {}", user.getUserId());
            return user.getUserId();
        } else {
            log.warn("Authentication is null");
        }
        return null;
    }
}
