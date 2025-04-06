package com.david.hlp.Spring.cloud.module.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.david.hlp.Spring.repeater.module.enums.HttpMethodType;
import com.david.hlp.Spring.common.result.BaseUser;
import lombok.experimental.SuperBuilder;
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CloudDTO<T extends BaseUser> {
    private T data;
    private String host;
    private String url;
    private String port;
    private String uniqueId;
    private String type;
    private String router;
    private String protocol;
    private HttpMethodType httpMethod;
}