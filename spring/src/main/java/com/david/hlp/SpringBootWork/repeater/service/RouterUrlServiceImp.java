package com.david.hlp.SpringBootWork.repeater.service;

import com.david.hlp.SpringBootWork.repeater.entity.RouterUrl;
import com.david.hlp.SpringBootWork.repeater.mapper.RouterUrlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouterUrlServiceImp {
    private final RouterUrlMapper routerUrlMapper;
    public List<RouterUrl> findAll() {
        return routerUrlMapper.findAll();
    }
    public RouterUrl findById(Integer id) {
        return routerUrlMapper.findById(id);
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

    public List<RouterUrl> listAll() {
        return routerUrlMapper.listAll();
    }
}
