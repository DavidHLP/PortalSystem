package com.david.hlp.Spring.cloud;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.david.hlp.Spring.repeater.entity.Repeater;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RepeaterServiceImp {
    private final RestTemplate restTemplate;
    private final BuildUrlService buildUrlService;

    public Object repeaterGet(Map<String, Object> params) {
        Repeater config = getRepeater("get");
        String finalUrl = buildUrlService.buildUrlWithParams(config.getUrl(), params);
        return restTemplate.getForObject(finalUrl, Object.class);
    }

    public Object repeaterPost(Map<String, Object> request) {
        Repeater config = getRepeater("post");
        return restTemplate.postForObject(config.getUrl(), request, Object.class);
    }

    public String repeaterDelete(Map<String, Object> request) {
        Repeater config = getRepeater("delete");
        restTemplate.delete(config.getUrl(), request);
        return "Delete successful";
    }

    public String repeaterPut(Map<String, Object> request) {
        Repeater config = getRepeater("put");
        restTemplate.put(config.getUrl(), request);
        return "Put successful";
    }

    private Repeater getRepeater(String type) {
        return switch (type) {
            case "get" -> buildUrlService.buildUrl(type);
            case "post" -> buildUrlService.buildUrl(type);
            case "delete" -> buildUrlService.buildUrl(type);
            case "put" -> buildUrlService.buildUrl(type);
            default -> throw new IllegalArgumentException("Unsupported request type: " + type);
        };
    }
}
