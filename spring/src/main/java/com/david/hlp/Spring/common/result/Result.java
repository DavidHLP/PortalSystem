package com.david.hlp.Spring.common.result;

import com.david.hlp.Spring.common.enums.ResultCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private int code;
    private String message;
    private T data;
    private long timestamp = System.currentTimeMillis();

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success() {
        return Result.<T>builder()
                .code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage())
                .build();
    }

    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    public static <T> Result<T> success(String message) {
        return Result.<T>builder()
                .code(ResultCode.SUCCESS.getCode())
                .message(message)
                .build();
    }

    public static <T> Result<T> error(int code, String message) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .build();
    }

    public static <T> Result<T> error(ResultCode code) {
        return Result.<T>builder()
                .code(code.getCode())
                .message(code.getMessage())
                .build();
    }

    public static <T> Result<T> error(ResultCode code, String customMessage) {
        return Result.<T>builder()
                .code(code.getCode())
                .message(customMessage)
                .build();
    }
}
