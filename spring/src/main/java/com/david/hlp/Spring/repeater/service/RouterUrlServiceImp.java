package com.david.hlp.Spring.repeater.service;

import com.david.hlp.Spring.repeater.entity.RouterUrl;
import com.david.hlp.Spring.repeater.mapper.RouterUrlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouterUrlServiceImp {
    private final RouterUrlMapper routerUrlMapper;
    public List<RouterUrl> findAll() {
        return routerUrlMapper.listRouterUrls();
    }
    public RouterUrl findById(Integer id) {
        return routerUrlMapper.getRouterUrlById(id);
    }
    public RouterUrl create(RouterUrl routerUrl) {
        routerUrl.setCreatedAt(LocalDateTime.now());
        routerUrlMapper.insert(routerUrl);
        return routerUrl;
    }
    public RouterUrl update(RouterUrl routerUrl) {
        routerUrlMapper.update(routerUrl);
        return routerUrl;
    }
    public void deleteById(Integer id) {
        routerUrlMapper.deleteById(id);
    }

    public List<RouterUrl> listRouterUrls() {
        return routerUrlMapper.listRouterUrls();
    }
}
