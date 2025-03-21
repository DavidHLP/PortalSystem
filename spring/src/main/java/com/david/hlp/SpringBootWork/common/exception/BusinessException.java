package com.david.hlp.SpringBootWork.common.exception;

import com.david.hlp.SpringBootWork.common.enums.ResultCode;

import lombok.Getter;

/**
 * 业务异常类
 */
@Getter
public class BusinessException extends RuntimeException {
    private ResultCode resultCode;
    private int code;
    private String message;
    
    /**
     * 使用ResultCode构造异常
     */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }
    
    /**
     * 使用字符串消息构造异常
     * 默认使用BAD_REQUEST(400)状态码
     */
    public BusinessException(String message) {
        super(message);
        this.resultCode = ResultCode.BAD_REQUEST;
        this.code = ResultCode.BAD_REQUEST.getCode();
        this.message = message;
    }
    
    /**
     * 使用自定义状态码和消息构造异常
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        // 使用最接近的ResultCode
        this.resultCode = ResultCode.BAD_REQUEST;
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
    public int getCode() {
        return this.code;
    }
}