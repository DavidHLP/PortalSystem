package com.david.hlp.SpringBootWork.repeater.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import com.david.hlp.SpringBootWork.repeater.entity.Repeater;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RepeaterServiceImp {

    @Component
    @Primary
    public static class DirectRestTemplate extends RestTemplate {
    }

    private final RestTemplate restTemplate;

    private final String demoUrl = "http://localhost:8080/api/demo/test";

    public String repeaterGet(Map<String, Object> params) {
        Repeater config = getRepeater("get");
        StringBuilder urlBuilder = new StringBuilder(config.getUrl());
        // 添加查询参数 (?key=value&key2=value2)
        if (!params.isEmpty()) {
            urlBuilder.append("?");
            int count = 0;
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (count > 0) {
                    urlBuilder.append("&");
                }
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue().toString());
                count++;
            }
        }
        String finalUrl = urlBuilder.toString();
        return restTemplate.getForObject(finalUrl, String.class);
    }

    public String repeaterPost(Map<String, Object> request) {
        Repeater config = getRepeater("post");
        return restTemplate.postForObject(config.getUrl(), request, String.class);
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
            case "get" -> buildUrl(type);
            case "post" -> buildUrl(type);
            case "delete" -> buildUrl(type);
            case "put" -> buildUrl(type);
            default -> throw new IllegalArgumentException("Unsupported request type: " + type);
        };
    }

    private Repeater buildUrl(String type) {
        // 去掉URL末尾的斜杠，确保不会误认为是静态资源
        String baseUrl = demoUrl;
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        return Repeater.builder()
                .type(type)
                .url(String.format("%s/%s", baseUrl, type))
                .build();
    }
}
