package com.david.hlp.Spring.repeater.module.dto;

import com.david.hlp.Spring.repeater.module.entity.Project;
import com.david.hlp.Spring.repeater.module.entity.RouterUrl;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class RouterProjectDTO extends RouterUrl {
    private List<Project> projects;
}
