package com.david.hlp.Spring.repeater.module.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum HttpMethodType {
    GET(0, "GET"),
    POST(1, "POST"),
    PUT(2, "PUT"),
    DELETE(3, "DELETE");

    @EnumValue
    private final Integer value;
    private final String desc;

    HttpMethodType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
