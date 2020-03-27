package com.iyb.retry.enums;

/**
 * TODO
 *
 * @author 2020/3/26 15:30 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public enum  RetryStatusEnum {

    /**
     * 重试成功 无异常有非空结果
     */
    SUCCEED("0"),

    /**
     * 重试失败 出现异常或 重试次数用尽
     */
    FAILED("1"),

    /**
     * 无异常 无结果
     */
    COMPLETE("2");

    private String status;

    RetryStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
