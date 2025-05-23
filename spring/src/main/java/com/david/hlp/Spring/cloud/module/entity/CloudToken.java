package com.david.hlp.Spring.cloud.module.entity;

import com.david.hlp.Spring.common.enums.TokenType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CloudToken implements Serializable {
    private String email;
    private String username;
    private String token;
    private TokenType tokenType;
    private boolean expired;
    private boolean revoked;
}