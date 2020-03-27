package com.iyb.retry.backoff;

import com.iyb.retry.RetryContext;

/**
 * BackOffPolicy
 *
 * @author 2020/3/27 11:10 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public interface BackOffPolicy {

    /**
     * 根据重试定义的补偿策略，创建补偿应用上下文
     *
     * @param retryContext 重试应用上下文
     * @return 补偿应用上下文
     */
    BackOffContext start(RetryContext retryContext);

    /**
     * 执行补偿
     *
     * @param backOffContext 补偿应用上下文
     */
    void backOff(BackOffContext backOffContext) throws BackOffInterruptedException;

}
