package com.david.hlp.Spring.repeater.entity;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Repeater {
    private String type;
    private String url;
    private String method;
    private String body;
    private String response;
    private String status;
    private String content;
}