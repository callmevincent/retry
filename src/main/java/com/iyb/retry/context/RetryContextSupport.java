package com.iyb.retry.context;

import com.iyb.retry.RetryConfiguration;
import com.iyb.retry.RetryContext;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * RetryContextSupport
 *
 * @author 2020/3/25 18:38 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public class RetryContextSupport implements RetryContext {

    private boolean terminate = false;

    private int retriedTimes;

    private Throwable lastException;

    private ProceedingJoinPoint point;


    private RetryConfiguration retryConfiguration;

    private String requestId;

    public RetryContextSupport(String requestId, ProceedingJoinPoint point, RetryConfiguration retryConfiguration) {
        this.requestId = requestId;
        this.point = point;
        this.retryConfiguration = retryConfiguration;
    }

    public Object doProceed() throws Throwable {
        return this.point.proceed();
    }

    public boolean isExhaustedOnly() {
        return terminate;
    }

    public void setExhaustedOnly() {
        terminate = true;
    }

    @Override
    public String getContextId() {
        return requestId;
    }

    /**
     * 获取重试次数
     *
     * @return 重试次数
     */
    @Override
    public int getRetryCount() {
        return retriedTimes;
    }

    /**
     * 触发本次重试的上次重试异常
     *
     * @return 获取最近一次重试异常
     */
    @Override
    public Throwable getLastThrowable() {
        return lastException;
    }

    /**
     * 获取重试配置
     */
    public RetryConfiguration getRetryConfiguration() {
        return retryConfiguration;
    }

    /**
     * 重试失败更新异常堆栈
     *
     * @param throwable 导致本次重试失败的异常
     */
    public void registerThrowable(Throwable throwable) {
        this.lastException = throwable;
        if (throwable != null) {
            retriedTimes++;
        }
    }

    @Override
    public String toString() {
        return String.format("[RetryContext: retriedTimes=%d, lastException=%s, exhausted=%b]", retriedTimes, lastException, terminate);
    }


}
