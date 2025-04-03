package com.david.hlp.Spring.repeater.module.entity;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {
    private Long id;
    private Long userId;
    private Long roleId;
    private Integer isDeleted;
}
