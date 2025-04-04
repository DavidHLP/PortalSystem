package com.david.hlp.Spring.repeater.module.dto;

import com.david.hlp.Spring.repeater.module.entity.RoleUrl;
import java.util.List;
import com.david.hlp.Spring.repeater.module.entity.RouterUrl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoleUrlRouterDTO extends RoleUrl {
    private List<RouterUrl> routerUrls;
}
