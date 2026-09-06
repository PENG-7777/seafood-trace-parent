package com.peng.admin.util;

import com.peng.admin.common.Result;
import com.peng.admin.common.ResultCode;

/**
 * 统一返回结果工具类
 * 对 Result<T> 静态工厂方法做一层封装，简化 Controller 编码
 * 风格对齐 Rust Result Ok / Err
 */
public class ResultUtil {

    // ====================== 成功快捷方法 ======================

    /**
     * 成功响应，不带返回数据
     * @return 成功Result对象
     * @param <T> 泛型
     */
    public static <T> Result<T> success() {
        return Result.ok();
    }

    /**
     * 成功响应，携带业务返回数据，默认提示【操作成功】
     * @param data 返回业务数据
     * @return Result
     * @param <T> 数据类型
     */
    public static <T> Result<T> success(T data) {
        return Result.ok(data);
    }

    /**
     * 成功响应，自定义提示文字 + 携带业务数据
     * @param msg 自定义成功提示
     * @param data 返回数据
     * @return Result
     * @param <T> 数据类型
     */
    public static <T> Result<T> success(String msg, T data) {
        return Result.ok(msg, data);
    }

    // ====================== 失败快捷方法 ======================

    /**
     * 使用预定义枚举返回失败结果
     * @param resultCode 响应码枚举
     * @return Result
     * @param <T> 泛型
     */
    public static <T> Result<T> fail(ResultCode resultCode) {
        return Result.err(resultCode);
    }

    /**
     * 自定义错误码 + 自定义错误信息返回失败
     * @param code 错误状态码
     * @param msg 错误提示
     * @return Result
     * @param <T> 泛型
     */
    public static <T> Result<T> fail(int code, String msg) {
        return Result.err(code, msg);
    }

    /**
     * 仅自定义错误消息，默认状态码500（FAIL）
     * @param msg 错误提示文本
     * @return Result
     * @param <T> 泛型
     */
    public static <T> Result<T> fail(String msg) {
        return Result.err(msg);
    }

    /**
     * 返回未登录401
     * @return Result
     * @param <T> 泛型
     */
    public static <T> Result<T> unauthorized() {
        return Result.err(ResultCode.UNAUTHORIZED);
    }

    /**
     * 返回参数错误400
     * @return Result
     * @param <T> 泛型
     */
    public static <T> Result<T> paramError() {
        return Result.err(ResultCode.PARAM_ERROR);
    }
}

