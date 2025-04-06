package com.david.hlp.Spring.cloud.module.dto;

import com.david.hlp.Spring.common.result.BaseUser;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CloudData<T> extends BaseUser {
    private T data;
}
