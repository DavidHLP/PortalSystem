package com.david.hlp.Spring.repeater.module.dto;

import com.david.hlp.Spring.repeater.module.entity.UserRoleProject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRoleProjectDTO extends UserRoleProject {
    /**
     * 密码
     */
    private String password;

}