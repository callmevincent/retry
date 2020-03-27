package com.iyb.retry.backoff;

/**
 * NoBackOffPolicy
 *
 * @author 2020/3/27 14:54 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public class NoBackOffPolicy extends AbstractBackOffPolicy {
    /**
     * 定义统一的方法调用
     */
    @Override
    protected void doBackOff() throws BackOffInterruptedException {
    }

    @Override
    public String toString() {
        return "NoBackOffPolicy{}";
    }
}
