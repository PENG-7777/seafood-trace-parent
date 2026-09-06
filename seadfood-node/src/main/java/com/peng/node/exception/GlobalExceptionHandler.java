package com.peng.node.exception;

import com.peng.node.util.Result;
import com.peng.node.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理器
 * 统一捕获Controller层抛出异常，包装为Rust风格Result<T>返回JSON
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务运行时异常，业务逻辑抛出 RuntimeException(message)
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        log.error("业务运行时异常：{}", e.getMessage(), e);
        // 使用500业务失败码，异常message作为提示
        return Result.err(e.getMessage());
    }

    /**
     * 捕获参数校验异常（例如@Valid参数错误）
     */
    @ExceptionHandler(org.springframework.validation.BindException.class)
    public Result<?> handleBindException(org.springframework.validation.BindException e) {
        String msg = e.getBindingResult().getFieldError() != null
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : ResultCode.PARAM_ERROR.getMsg();
        log.error("参数校验异常：{}", msg, e);
        return Result.err(ResultCode.PARAM_ERROR.getCode(), msg);
    }

    /**
     * 捕获非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("非法参数异常：{}", e.getMessage(), e);
        return Result.err(ResultCode.PARAM_ERROR.getCode(), e.getMessage());
    }

    /**
     * 捕获全部其他未知系统异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统未知异常：", e);
        return Result.err(ResultCode.FAIL);
    }
}
