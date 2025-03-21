package com.david.hlp.SpringBootWork.common.exception;

import com.david.hlp.SpringBootWork.common.enums.ResultCode;

public class ApiException extends RuntimeException {
    private final ResultCode resultCode;

    public ApiException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}