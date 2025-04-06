package com.david.hlp.Spring.cloud.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.david.hlp.Spring.cloud.module.dto.CloudDTO;
import com.david.hlp.Spring.cloud.module.dto.CloudData;
import java.util.HashMap;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class CloudServiceImpl {
    private final RestTemplate restTemplate;
    public HashMap<String, Object> post(CloudDTO<CloudData<HashMap<String, Object>>> cloudDTO) {
        Object result = restTemplate.postForObject(cloudDTO.getUrl(), cloudDTO.getData(), Object.class);
        if (result != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(result, new TypeReference<HashMap<String, Object>>() {});
        }
        return new HashMap<>();
    }
}
