package com.peng.admin.common;

/**
 * 响应码枚举，配合Result统一返回类使用
 */
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    UNAUTHORIZED(401, "未登录或Token已失效，请重新登录"),
    PARAM_ERROR(400, "请求参数校验错误"),
    NOT_FOUND(404, "请求资源不存在"),
    BUSINESS_ERROR(600, "业务逻辑处理异常");

    /** 业务状态码 */
    private final int code;
    /** 状态描述信息 */
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

