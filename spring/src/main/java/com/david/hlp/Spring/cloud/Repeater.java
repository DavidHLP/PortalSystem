package com.david.hlp.Spring.cloud;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Repeater {
    private String url;
    private String uniqueId;
    private String type;
    private String response;
    private String status;
}