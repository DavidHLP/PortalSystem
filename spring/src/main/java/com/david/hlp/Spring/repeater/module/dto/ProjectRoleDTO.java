package com.david.hlp.Spring.repeater.module.dto;

import com.david.hlp.Spring.repeater.module.entity.Project;
import com.david.hlp.Spring.repeater.module.entity.RoleUrl;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRoleDTO extends Project {
    private List<RoleUrl> roleUrls;
}