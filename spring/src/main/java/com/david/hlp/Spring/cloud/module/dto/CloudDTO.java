package com.david.hlp.Spring.cloud.module.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.david.hlp.Spring.repeater.module.enums.HttpMethodType;
import com.david.hlp.Spring.common.result.BaseUser;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CloudDTO<T extends BaseUser> implements Serializable {
    private T data;
    private String host;
    private String port;
    private String uniqueId;
    private String type;
    private String router;
    private String protocol;
    private HttpMethodType httpMethod;

    public String getUrl() {
        return this.protocol + "://" + this.host + ":" + this.port + this.router;
    }
}