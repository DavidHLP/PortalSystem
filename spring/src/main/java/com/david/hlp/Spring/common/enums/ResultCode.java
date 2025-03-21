package com.david.hlp.Spring.common.enums;

public enum ResultCode {
    /* 成功状态码 */
    SUCCESS(200, "操作成功"),

    /* 客户端错误 4xx */
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权访问"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法错误"),
    /* 服务端错误 5xx */
    INTERNAL_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    /* 自定义业务错误 10xx */
    USER_EXISTS(1001, "用户已存在"),
    INVALID_CREDENTIALS(1002, "用户名或密码错误"),
    CAPTCHA_ERROR(1003, "验证码错误"),
    USER_NOT_FOUND(1004, "用户不存在"),
    PASSWORD_ERROR(1005, "密码错误");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // Getter方法
    public int getCode() { return code; }
    public String getMessage() { return message; }
}
