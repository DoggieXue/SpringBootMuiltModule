package org.cloud.xue.simplespringboot.entity;

/**
 * 常用结果枚举
 */
public enum ResultEnum implements IResult{
    SUCCESS("0", "交易成功"),
    VALIDATE_FAILED("1001", "参数校验失败"),
    COMMON_FAILED("1002", "接口调用失败"),
    FORBIDDEN("1003", "没有权限访问资源")
    ;

    private String code;
    private String msg;

    ResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }
    @Override
    public String getMsg() {
        return msg;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
