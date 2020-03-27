package com.iyb.retry.backoff;

/**
 * 定义统一的休眠方法
 *
 * @author 2020/3/27 11:15 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public interface Sleeper {

    /**
     * 休眠
     *
     * @param period    休眠时长
     * @throws InterruptedException .
     */
    void sleep(long period) throws InterruptedException;
}
