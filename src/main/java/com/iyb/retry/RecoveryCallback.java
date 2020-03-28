package com.iyb.retry;

/**
 * RecoveryCallBack
 *
 * @author 2020/3/25 13:11 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public interface RecoveryCallback<T> {

    /**
     * 重试失败降级
     *
     * @param context 重试上下文
     * @return 重试失败后的默认返回
     * @throws Exception 重试异常
     */
    T recover(RetryContext context) throws Exception;
}
