package com.iyb.retry.backoff;

/**
 * SleepingBackOffPolicy
 *
 * @author 2020/3/27 11:25 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public interface SleepingBackOffPolicy<T extends SleepingBackOffPolicy<T>> extends BackOffPolicy{

    /**
     * 重新获取当前BackOffPolicy的Sleeper
     *
     * @param sleeper .
     * @return .
     */
    T retrieveSleeper(Sleeper sleeper);
}
