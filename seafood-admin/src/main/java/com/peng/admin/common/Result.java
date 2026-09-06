package com.peng.admin.common;

import lombok.Data;

/**
 * 统一返回结果封装类 Result
 * 借鉴Rust Result<T> 编码风格；区分Ok成功 / Err失败
 * JSON返回格式示例:
 * 成功: {"ok":true,"code":200,"msg":"操作成功","data":{}}
 * 失败: {"ok":false,"code":500,"msg":"操作失败","data":null}
 * @param <T> 泛型，代表返回data域的数据类型
 */
@Data
public class Result<T> {

    /**
     * 是否执行成功，对标Rust Result的Ok/Err标记
     * true=成功；false=失败
     */
    private Boolean ok;

    /**
     * 业务状态码：200成功，401未登录，400参数错误，500业务失败
     */
    private Integer code;

    /**
     * 提示消息，成功/失败的文字描述
     */
    private String msg;

    /**
     * 返回业务数据；失败时data为null
     */
    private T data;


    // -------------------------- 静态工厂方法：构建成功返回对象 --------------------------

    /**
     * 成功响应，携带返回数据，使用默认成功消息【操作成功】
     * @param data 需要返回给前端的数据
     * @return Result<T> 封装好的成功结果对象
     * @param <T> 数据泛型类型
     */
    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setOk(true);
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    /**
     * 成功响应，无返回数据，只返回成功状态与提示
     * @return Result<T>
     */
    public static <T> Result<T> ok() {
        return ok(null);
    }

    /**
     * 成功响应，自定义提示消息，同时携带业务数据
     * @param msg 自定义成功提示文本
     * @param data 返回数据
     * @return Result<T>
     */
    public static <T> Result<T> ok(String msg, T data) {
        Result<T> result = new Result<>();
        result.setOk(true);
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(msg);
        result.setData(data);
        return result;
    }


    // -------------------------- 静态工厂方法：构建失败返回对象 --------------------------

    /**
     * 失败响应，传入预定义的错误枚举
     * @param resultCode 错误枚举对象 ResultCode
     * @return Result<T>
     */
    public static <T> Result<T> err(ResultCode resultCode) {
        Result<T> result = new Result<>();
        result.setOk(false);
        result.setCode(resultCode.getCode());
        result.setMsg(resultCode.getMsg());
        result.setData(null);
        return result;
    }

    /**
     * 失败响应：自定义业务状态码 + 自定义错误消息
     * @param code 自定义错误码
     * @param msg 自定义错误提示
     * @return Result<T>
     */
    public static <T> Result<T> err(int code, String msg) {
        Result<T> result = new Result<>();
        result.setOk(false);
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    /**
     * 失败响应：只填写错误消息，使用默认500失败状态码
     * @param msg 错误提示文字
     * @return Result<T>
     */
    public static <T> Result<T> err(String msg) {
        return err(ResultCode.FAIL.getCode(), msg);
    }


    // -------------------------- 实例成员方法，模仿Rust Result API --------------------------

    /**
     * 判断当前结果是否为成功Ok
     * @return true 成功；false 失败
     */
    public boolean isOk() {
        return Boolean.TRUE.equals(this.ok);
    }

    /**
     * 判断当前结果是否为错误Err
     * @return true 失败；false 成功
     */
    public boolean isErr() {
        return !isOk();
    }

    /**
     * 模拟Rust unwrap()
     * 如果是Ok，直接返回data数据；如果是Err，抛出运行时异常
     * @return T data业务数据
     */
    public T unwrap() {
        if (isErr()) {
            throw new RuntimeException("Result.unwrap()错误：code=" + this.code + "，msg=" + this.msg);
        }
        return this.data;
    }

    /**
     * 模拟Rust unwrapOr()
     * Ok则返回data；Err返回传入的默认值，不会抛异常
     * @param defaultValue 失败时返回的默认值
     * @return T data或者defaultValue
     */
    public T unwrapOr(T defaultValue) {
        if (isOk()) {
            return this.data;
        }
        return defaultValue;
    }
}

