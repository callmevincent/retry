package com.iyb.retry.policy;

import com.iyb.retry.RetryContext;
import com.iyb.retry.RetryPolicy;
import com.iyb.retry.context.DefaultRetryContext;

/**
 * MaxAttemptsRetryPolicy
 *
 * @author 2020/3/27 14:40 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public class MaxAttemptsRetryPolicy implements RetryPolicy {
    public final static int DEFAULT_MAX_ATTEMPTS = 3;

    private volatile int maxAttempts;

    public MaxAttemptsRetryPolicy() {
        this.maxAttempts = DEFAULT_MAX_ATTEMPTS;
    }

    /**
     * 指定重试次数
     * @param maxAttempts 次数
     */
    public MaxAttemptsRetryPolicy(int maxAttempts) {
        this.maxAttempts = maxAttempts > 0 ? maxAttempts : 1;
    }

    /**
     * 是否支持重试
     *
     * @param context 当前重试上下文
     * @return true 支持重试
     */
    @Override
    public boolean canRetry(RetryContext context) {
        return context.getRetryCount() < this.maxAttempts;
    }

    /**
     * 获取重试上下文（相关参数）
     *
     * @param parent 链式
     * @return a {@link RetryContext} object specific to this policy.
     */
    @Override
    public RetryContext open(RetryContext parent) {
        return new DefaultRetryContext(parent);
    }

    /**
     * @param context 重试上下文
     */
    @Override
    public void close(RetryContext context) {

    }

    /**
     * 单次重试失败记录
     *
     * @param context   重试上下文
     * @param throwable 异常
     */
    @Override
    public void registerThrowable(RetryContext context, Throwable throwable) {
        ((DefaultRetryContext) context).registerThrowable(throwable);
    }
}
