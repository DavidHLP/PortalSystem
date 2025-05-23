package com.david.hlp.cloud.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private String username;
    private PasswordDTO data;
    private String email;
}
