package com.david.hlp.Spring.repeater.module.dto;

import java.util.List;

import com.david.hlp.Spring.repeater.module.entity.RoleUrl;
import com.david.hlp.Spring.repeater.module.entity.RouterUrl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RoleUrlDTO extends RoleUrl {
    private List<RouterUrl> routers;
}