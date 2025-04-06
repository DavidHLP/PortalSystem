package com.david.hlp.Spring.common.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.david.hlp.Spring.common.result.Result;
import com.david.hlp.Spring.common.enums.ResultCode;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;
import java.util.Optional;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

/**
 * 全局异常处理器
 *
 * @author david
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String PARAM_ERROR_MSG = "参数错误";
    private static final String DATE_FORMAT_ERROR_MSG = "日期时间格式错误，请使用正确的格式 (yyyy-MM-dd'T'HH:mm:ss)";
    private static final String DB_ERROR_MSG = "数据库操作异常，请联系管理员";
    private static final String TOKEN_EXPIRED_MSG = "登录已过期，请重新登录";
    private static final String TOKEN_INVALID_MSG = "无效的身份令牌";
    private static final String TOKEN_SIGNATURE_MSG = "身份验证失败，令牌签名无效";
    private static final String TOKEN_UNSUPPORTED_MSG = "不支持的令牌格式";

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBusinessException(final BusinessException ex) {
        log.error("业务异常: {}", ex.getMessage());
        return Result.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBindException(final BindException ex) {
        FieldError fieldError = ex.getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : PARAM_ERROR_MSG;
        log.error("参数绑定异常: {}", message);
        return Result.error(ResultCode.BAD_REQUEST, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleValidationException(final MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("参数校验失败: {}", errorMsg);
        return Result.error(ResultCode.BAD_REQUEST, errorMsg);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException ex) {
        log.warn("参数类型不匹配: {}", ex.getMessage());
        String errorMsg = String.format("参数'%s'类型错误，应为: %s", ex.getName(),
            Optional.ofNullable(ex.getRequiredType())
                .map(Class::getSimpleName)
                .orElse("未知类型"));
        return Result.error(ResultCode.BAD_REQUEST, errorMsg);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleHttpMessageNotReadableException(final HttpMessageNotReadableException ex) {
        String errorMessage = ex.getMessage();
        log.warn("请求数据格式错误: {}", errorMessage);

        if (errorMessage != null && errorMessage.contains("java.time.LocalDateTime")) {
            return Result.error(ResultCode.BAD_REQUEST, DATE_FORMAT_ERROR_MSG);
        }
        return Result.error(ResultCode.BAD_REQUEST, "请求数据格式错误: " + errorMessage);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleMissingServletRequestParameterException(final MissingServletRequestParameterException ex) {
        log.warn("缺少必要参数: {}", ex.getMessage());
        return Result.error(ResultCode.BAD_REQUEST, String.format("缺少必要参数: %s", ex.getParameterName()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<?> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException ex) {
        log.warn("请求方法不支持: {}", ex.getMessage());
        return Result.error(ResultCode.METHOD_NOT_ALLOWED,
            String.format("不支持%s请求方法，支持的方法: %s",
                ex.getMethod(),
                String.join(", ", ex.getSupportedMethods() != null ? ex.getSupportedMethods() : new String[0])));
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleDataAccessException(final DataAccessException ex) {
        log.error("数据库操作异常", ex);
        return Result.error(ResultCode.INTERNAL_ERROR, DB_ERROR_MSG);
    }

    /**
     * 处理JWT令牌过期异常
     */
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<?> handleExpiredJwtException(final ExpiredJwtException ex) {
        log.warn("JWT令牌已过期: {}", ex.getMessage());
        return Result.error(ResultCode.UNAUTHORIZED, TOKEN_EXPIRED_MSG);
    }

    /**
     * 处理JWT签名无效异常
     */
    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<?> handleSignatureException(final SignatureException ex) {
        log.warn("JWT签名验证失败: {}", ex.getMessage());
        return Result.error(ResultCode.UNAUTHORIZED, TOKEN_SIGNATURE_MSG);
    }

    /**
     * 处理JWT格式错误异常
     */
    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<?> handleMalformedJwtException(final MalformedJwtException ex) {
        log.warn("JWT格式错误: {}", ex.getMessage());
        return Result.error(ResultCode.UNAUTHORIZED, TOKEN_INVALID_MSG);
    }

    /**
     * 处理不支持的JWT异常
     */
    @ExceptionHandler(UnsupportedJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<?> handleUnsupportedJwtException(final UnsupportedJwtException ex) {
        log.warn("不支持的JWT令牌: {}", ex.getMessage());
        return Result.error(ResultCode.UNAUTHORIZED, TOKEN_UNSUPPORTED_MSG);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(final Exception ex) {
        log.error("系统异常", ex);
        return Result.error(ResultCode.INTERNAL_ERROR);
    }
}