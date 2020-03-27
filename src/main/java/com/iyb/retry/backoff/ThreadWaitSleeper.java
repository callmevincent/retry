package com.iyb.retry.backoff;

/**
 * ThreadWaitSleeper
 *
 * @author 2020/3/27 11:19 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public class ThreadWaitSleeper implements Sleeper {
    /**
     * 休眠
     *
     * @param period 休眠时长
     * @throws InterruptedException .
     */
    @Override
    public void sleep(long period) throws InterruptedException {
        Thread.sleep(period);
    }
}
