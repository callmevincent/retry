package com.iyb.retry;

import java.io.Serializable;

/**
 * 重试策略
 *
 * @author 2020/3/25 13:06 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public interface RetryPolicy extends Serializable {

    /**
     * 是否支持重试
     *
     * @param context 当前重试上下文
     * @return true 支持重试
     */
    boolean canRetry(RetryContext context);

    /**
     * 获取重试上下文（相关参数）
     *
     * @param parent 嵌套的父级重试上下文 TODO 支持嵌套重试
     * @return a {@link RetryContext} object specific to this policy.
     */
    RetryContext open(RetryContext parent);

    /**
     * @param context 重试上下文
     */
    void close(RetryContext context);

    /**
     * 单次重试失败记录
     *
     * @param context   重试上下文
     * @param throwable 异常
     */
    void registerThrowable(RetryContext context, Throwable throwable);
}