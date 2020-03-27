package com.iyb.retry.backoff;

import com.iyb.retry.RetryContext;

/**
 * DefaultBackOffPolicy 考虑抽象一个执行接口方法 backOff 的抽象方法 doBackOff()
 *
 * @author 2020/3/27 11:39 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public abstract class AbstractBackOffPolicy implements BackOffPolicy {
    /**
     * 根据重试定义的补偿策略，创建补偿应用上下文
     * 子类选择性重写
     *
     * @param retryContext 重试应用上下文
     * @return 补偿应用上下文
     */
    @Override
    public BackOffContext start(RetryContext retryContext) {
        return null;
    }

    /**
     * 执行补偿
     * 子类选择性重写
     *
     * @param backOffContext 补偿应用上下文
     */
    @Override
    public void backOff(BackOffContext backOffContext)  throws BackOffInterruptedException {
        doBackOff();
    }

    /**
     * 定义统一的方法调用
     */
    protected abstract void doBackOff() throws BackOffInterruptedException;
}
