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
public class PasswordDTO implements Serializable {
    private String password;
}
