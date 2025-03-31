package com.david.hlp.Spring.cloud;

import com.david.hlp.Spring.repeater.entity.Repeater;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class BuildUrlService {
    private final String demoUrl = "http://localhost:9097/test/demo/test";

    public Repeater buildUrl(String type) {
        String baseUrl = demoUrl;
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        return Repeater.builder()
                .type(type)
                .url(String.format("%s/%s", baseUrl, type))
                .build();
    }

    public String buildUrlWithParams(String baseUrl, Map<String, Object> params) {
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
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
        return urlBuilder.toString();
    }
}
