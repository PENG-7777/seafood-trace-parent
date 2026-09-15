package com.peng.node.exception;

import com.peng.node.util.ResultCode;

public class BusinessException extends RuntimeException {
    private final ResultCode resultCode;

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    // 新增重载构造：直接传入自定义字符串消息
    public BusinessException(String message) {
        super(message);
        this.resultCode = null;
    }
}
