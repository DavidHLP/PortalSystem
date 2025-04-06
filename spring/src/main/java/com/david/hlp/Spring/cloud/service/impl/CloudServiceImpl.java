package com.david.hlp.Spring.cloud.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.david.hlp.Spring.cloud.module.dto.CloudDTO;
import com.david.hlp.Spring.cloud.module.dto.CloudData;
import com.david.hlp.Spring.cloud.mapper.CloudRouterUrlMapper;
import java.util.HashMap;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.david.hlp.Spring.cloud.module.dto.CloudRequest;

@Service
@RequiredArgsConstructor
public class CloudServiceImpl {
    private final RestTemplate restTemplate;
    private final CloudRouterUrlMapper cloudRouterUrlMapper;
    public HashMap<String, Object> post(CloudRequest cloudRequest, String userEmail) {
        CloudDTO<CloudData<HashMap<String, Object>>> cloudRouterUrlDTO = cloudRouterUrlMapper.selectByUniqueIdAndUserId(cloudRequest.getUniqueId(), userEmail);
        if (cloudRouterUrlDTO == null) {
            throw new RuntimeException("未找到对应的路由信息");
        }
        cloudRouterUrlDTO.getData().setData(cloudRequest.getData());
        System.out.println("cloudRouterUrlDTO: " + cloudRouterUrlDTO);
        Object result = restTemplate.postForObject(cloudRouterUrlDTO.getUrl(), cloudRouterUrlDTO.getData(), Object.class);
        if (result != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(result, new TypeReference<HashMap<String, Object>>() {});
        }
        return new HashMap<>();
    }
}
