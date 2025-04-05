package com.david.hlp.Spring.repeater.module.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouterProject {
    private Long id;
    private Long routerId;
    private Long projectId;
}
