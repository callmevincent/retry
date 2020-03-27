package com.iyb.retry;

/**
 * 重试回调
 *
 * @author 2020/3/25 13:11 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public interface RetryCallback<T, E extends Throwable> {
    /**
     * @param context 重试上下文
     * @return 重试结果
     * @throws E 重试异常
     */
    T doWithRetry(RetryContext context) throws E;
}
