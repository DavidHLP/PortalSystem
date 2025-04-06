package com.david.hlp.Spring.cloud.module.entity;

import com.david.hlp.Spring.common.result.BaseUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoginRequest extends BaseUser {
    private Long id;
    private String email;
    private String username;
    private String password;
}
