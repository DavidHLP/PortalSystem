package com.david.hlp.SpringBootWork.repeater.entity;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Url {
    private Integer id;
    private String protocol;
    private Integer hostId;
    private HostUrl host;
    private Integer portId;
    private PortUrl port;
    private Integer routerId;
    private RouterUrl router;
    private Integer projectId;
    private ProjectUrl project;
    private String method;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private String description;
}