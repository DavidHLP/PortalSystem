package com.david.hlp.SpringBootWork.common.exception;

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

import com.david.hlp.SpringBootWork.common.result.Result;
import com.david.hlp.SpringBootWork.common.enums.ResultCode;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBindException(BindException e) {
        FieldError fieldError = e.getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数错误";
        log.error("参数绑定异常: {}", message);
        return Result.error(ResultCode.BAD_REQUEST, message);
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        logger.warn("参数校验失败: {}", errorMsg);
        return Result.error(ResultCode.BAD_REQUEST, errorMsg);
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        logger.warn("参数类型不匹配: {}", e.getMessage());
        String errorMsg = String.format("参数'%s'类型错误，应为: %s", e.getName(), e.getRequiredType().getSimpleName());
        return Result.error(ResultCode.BAD_REQUEST, errorMsg);
    }
    
    /**
     * 处理JSON解析异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = ex.getMessage();
        logger.warn("请求数据格式错误: {}", errorMessage);
        
        // 特殊处理LocalDateTime解析错误
        if (errorMessage != null && errorMessage.contains("java.time.LocalDateTime")) {
            return Result.error(ResultCode.BAD_REQUEST, "日期时间格式错误，请使用正确的格式 (yyyy-MM-dd'T'HH:mm:ss)");
        }
        return Result.error(ResultCode.BAD_REQUEST, "请求数据格式错误: " + errorMessage);
    }
    
    /**
     * 处理缺少必要参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        logger.warn("缺少必要参数: {}", ex.getMessage());
        return Result.error(ResultCode.BAD_REQUEST, String.format("缺少必要参数: %s", ex.getParameterName()));
    }
    
    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        logger.warn("请求方法不支持: {}", ex.getMessage());
        return Result.error(ResultCode.METHOD_NOT_ALLOWED, 
            String.format("不支持%s请求方法，支持的方法: %s", 
                ex.getMethod(), String.join(", ", ex.getSupportedMethods())));
    }
    
    /**
     * 处理数据库操作异常
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleDataAccessException(DataAccessException ex) {
        logger.error("数据库操作异常", ex);
        return Result.error(ResultCode.INTERNAL_ERROR, "数据库操作异常，请联系管理员");
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        logger.error("系统异常", e);
        return Result.error(ResultCode.INTERNAL_ERROR);
    }
}