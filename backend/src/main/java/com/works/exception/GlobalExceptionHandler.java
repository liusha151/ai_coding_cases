package com.works.exception;

import com.works.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器：将业务异常和未预期异常统一转换为 Result 格式返回
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /** 捕获业务异常，将错误码和信息返回给前端 */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(), e.getMessage());
    }
    /** 捕获未预料的系统异常，返回 500 错误 */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        return Result.error(500, "服务器内部错误：" + e.getMessage());
    }
}
