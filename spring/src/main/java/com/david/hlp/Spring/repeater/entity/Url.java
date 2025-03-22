package com.david.hlp.Spring.repeater.entity;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.io.Serializable;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Url implements Serializable {
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