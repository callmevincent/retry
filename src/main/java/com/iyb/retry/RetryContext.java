package com.iyb.retry;

import com.alibaba.fastjson.JSONObject;
import com.iyb.retry.bean.RetryResult;
import com.iyb.retry.enums.RetryStatusEnum;

/**
 * RetryContext
 *
 * @author 2020/3/25 13:08 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public interface RetryContext {

    /**
     * contextId
     */
    String getContextId();

    /**
     * 暴露此方法到接口层 方便执行器调用
     */
    void setExhaustedOnly();

    /**
     * 暴露此方法到接口层 方便执行器调用
     * @return .
     */
    boolean isExhaustedOnly();

    /**
     *
     * @return
     */
    RetryContext getParent();

    /**
     * 获取重试次数
     * @return 重试次数
     */
    int getRetryCount();

    /**
     * 触发本次重试的上次重试异常
     * @return 获取最近一次重试异常
     */
    Throwable getLastThrowable();

    /**
     * 获取重试配置
     */
    RetryConfiguration getRetryConfiguration();

    /**
     * 构建邮件消息
     *
     * @param title
     * @param content
     * @return
     */
    JSONObject buildMailParams(String title, String content);

    void setRetryStatus(RetryStatusEnum status);

    /**
     * 判断最终重试状态
     *
     * @return .
     */
    RetryResult getRetryResult(Object result);
}
