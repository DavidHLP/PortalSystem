package com.david.hlp.Spring.common.result;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseUser {
    private String username;
    private String password;
    private String email;
}
