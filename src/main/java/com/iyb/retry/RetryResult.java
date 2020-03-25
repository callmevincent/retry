package com.iyb.retry;

import java.util.Date;

/**
 * TODO
 *
 * @author 2020/3/25 15:28 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public class RetryResult {
    private Long id;

    /**
     * 请求id
     */
    private String requestId;

    private String clazz;

    private String method;

    /**
     * 重试状态
     *  0   失败
     *  1   成功
     *  2   超时
     *  3   重试无果
     */
    private String status;

    /**
     * 重试次数（成功、超时时的已重试次数或设置的重试阈值）
     */
    private int retryTimes;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 重试结果
     */
    private String result;

    private Date gmtCreated;

    public RetryResult(Long id, String requestId, String clazz, String method, String status, int retryTimes, String errorMsg, String result, Date gmtCreated) {
        this.id = id;
        this.requestId = requestId;
        this.clazz = clazz;
        this.method = method;
        this.status = status;
        this.retryTimes = retryTimes;
        this.errorMsg = errorMsg;
        this.result = result;
        this.gmtCreated = gmtCreated;
    }

    public RetryResult(Builder builder) {
        this.id = builder.id;
        this.requestId = builder.requestId;
        this.clazz = builder.clazz;
        this.method = builder.method;
        this.status = builder.status;
        this.retryTimes = builder.retryTimes;
        this.errorMsg = builder.errorMsg;
        this.result = builder.result;
        this.gmtCreated = builder.gmtCreated;
        if (gmtCreated == null) {
            this.gmtCreated = new Date();
        }
    }

    public static Builder builder(String requestId, String status, String result) {
        return new Builder(requestId, status, result);
    }

    /**
     * RetryResult Builder
     */
    public static class Builder {
        private Long id;
        private final String requestId;
        private String clazz;
        private String method;
        private final String status;
        private int retryTimes;
        private String errorMsg;
        private final String result;
        private Date gmtCreated;

        public Builder(String requestId, String status, String result) {
            this.requestId = requestId;
            this.status = status;
            this.result = result;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder clazz(String clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder retryTimes(int retryTimes) {
            this.retryTimes = retryTimes;
            return this;
        }

        public Builder errorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
            return this;
        }

        public Builder gmtCreated(Date gmtCreated) {
            this.gmtCreated = gmtCreated;
            return this;
        }

        public RetryResult build() {
            return new RetryResult(this);
        }
    }

    @Override
    public String toString() {
        return "RetryResult{" +
                "id=" + id +
                ", requestId='" + requestId + '\'' +
                ", clazz='" + clazz + '\'' +
                ", method='" + method + '\'' +
                ", status='" + status + '\'' +
                ", retryTimes=" + retryTimes +
                ", errorMsg='" + errorMsg + '\'' +
                ", result='" + result + '\'' +
                ", gmtCreated=" + gmtCreated +
                '}';
    }
}
