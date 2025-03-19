package com.david.hlp.SpringBootWork.system.token;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;

public enum TokenType implements IEnum<String> {
    BEARER("BEARER"),
    ACCESS("ACCESS"),
    REFRESH("REFRESH");
    @EnumValue
    private final String value;

    TokenType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}