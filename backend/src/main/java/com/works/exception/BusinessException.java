package com.works.exception;

/** 业务异常：携带错误码和错误信息，由 GlobalExceptionHandler 统一处理 */
public class BusinessException extends RuntimeException {
    private int code;
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
    public int getCode() { return code; }
}
