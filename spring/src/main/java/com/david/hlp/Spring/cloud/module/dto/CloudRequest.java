package com.david.hlp.Spring.cloud.module.dto;

import java.io.Serializable;

import lombok.Data;

import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CloudRequest implements Serializable {
    private String uniqueId;
    private HashMap<String, Object> data;
}
