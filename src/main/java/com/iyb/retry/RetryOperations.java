package com.iyb.retry;

/**
 * RetryOperations
 *
 * @author 2020/3/26 12:34 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public interface RetryOperations {
    /**
     * 无默认值重试
     *
     * @param <T>           返回结果
     * @param retryCallback 回调方法
     * @param <E>           异常
     * @return 重试回调函数执行成功后的返回结果
     * @throws E 重试过程中出现的异常
     */
    <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback) throws E;
}
