package com.david.hlp.cloud.entity;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String username;
    private TestData data;
    private String email;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TestData {
        private Integer password;
    }
}
