package com.iyb.retry;

import org.springframework.util.backoff.BackOff;

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
}
