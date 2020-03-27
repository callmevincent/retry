package com.iyb.retry.policy;

import com.iyb.retry.RetryContext;
import com.iyb.retry.RetryPolicy;
import com.iyb.retry.context.DefaultRetryContext;

import java.util.Collections;
import java.util.Map;

/**
 * SimpleRetryPolicy 基于重试次数
 *
 * @author 2020/3/26 10:42 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public class SimpleRetryPolicy implements RetryPolicy {

    public final static int DEFAULT_MAX_ATTEMPTS = 3;

    private volatile int maxAttempts;

    private Map<Class<? extends Throwable>, Boolean> typeMap;

    /**
     * 使用默认值创建
     */
    public SimpleRetryPolicy() {
        this(DEFAULT_MAX_ATTEMPTS);
    }

    /**
     * 指定重试次数
     *
     * @param maxAttempts the maximum number of attempts
     */
    public SimpleRetryPolicy(int maxAttempts) {
        this(maxAttempts, null);
    }

    /**
     * 指定重试次数
     *
     * @param maxAttempts the maximum number of attempts
     */
    public SimpleRetryPolicy(int maxAttempts, Map<Class<? extends Throwable>, Boolean> typeMap) {
        this.maxAttempts = maxAttempts;
        this.typeMap = Collections.singletonMap(Exception.class, true);
        if (typeMap != null) {
            this.typeMap.putAll(typeMap);
        }
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public int getMaxAttempts() {
        return this.maxAttempts;
    }

    /**
     * 是否可以重试
     *
     * @return true 可以重试
     */
    @Override
    public boolean canRetry(RetryContext context) {
        Throwable t = context.getLastThrowable();
        return (t == null || retryForException(t)) && context.getRetryCount() < this.maxAttempts;
    }

    /**
     *
     */
    @Override
    public void close(RetryContext status) {
    }

    /**
     *
     * @param context   重试上下文
     * @param throwable 异常
     */
    @Override
    public void registerThrowable(RetryContext context, Throwable throwable) {
        SimpleRetryContext simpleContext = ((SimpleRetryContext) context);
        simpleContext.registerThrowable(throwable);
    }

    /**
     *
     */
    @Override
    public RetryContext open(RetryContext parent) {
        return new SimpleRetryContext(parent);
    }

    private static class SimpleRetryContext extends DefaultRetryContext {

        public SimpleRetryContext(RetryContext parent) {
            super(parent);
        }
    }

    /**
     * 判断发生异常是否支持重试
     *
     * @param ex 异常
     * @return 重试异常是否是指定异常（或子类）
     */
    private boolean retryForException(Throwable ex) {
        //TODO
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[maxAttempts=" + this.maxAttempts + "]";
    }

}
