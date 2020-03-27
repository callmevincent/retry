package com.iyb.retry.support;

import com.iyb.retry.*;
import com.iyb.retry.backoff.BackOffContext;
import com.iyb.retry.backoff.BackOffInterruptedException;
import com.iyb.retry.backoff.BackOffPolicy;
import com.iyb.retry.backoff.NoBackOffPolicy;
import com.iyb.retry.enums.RetryStatusEnum;
import com.iyb.retry.policy.MaxAttemptsRetryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * RetryTemplate
 *
 * @author 2020/3/26 12:09 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public class RetryTemplate implements RetryOperations {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private volatile RetryPolicy retryPolicy = new MaxAttemptsRetryPolicy(3);

    private volatile BackOffPolicy backOffPolicy = new NoBackOffPolicy();

    private boolean throwLastExceptionOnExhausted;

    private RetryContext retryContext;

    public RetryTemplate() {
    }

    public static RetryTemplateBuilder builder(RetryContext originRetryContext) {
        return new RetryTemplateBuilder().originRetryContext(originRetryContext);
    }

    public static RetryTemplate defaultInstance(RetryContext originRetryContext) {
        return builder(originRetryContext).build();
    }

    /**
     *
     */
    @Override
    public final <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback) throws E {
        return doExecute(retryCallback);
    }

    public RetryContext getContext() {
        return retryContext;
    }

    /**
     * @param retryCallback
     * @param <T>
     * @param <E>
     * @return
     * @throws E
     * @throws ExhaustedRetryException
     */
    protected <T, E extends Throwable> T doExecute(RetryCallback<T, E> retryCallback) throws E, ExhaustedRetryException {
        RetryPolicy retryPolicy = this.retryPolicy;
        BackOffPolicy backOffPolicy = this.backOffPolicy;
        RetryContext context = retryPolicy.open(retryContext);
        Throwable lastException = null;
        boolean exhausted = false;
        try {
            BackOffContext backOffContext = backOffPolicy.start(context);
            while (canRetry(retryPolicy, context)) {
                try {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("context {} 已重试 {} 次", context.getContextId(), context.getRetryCount());
                    }
                    lastException = null;
                    return retryCallback.doWithRetry(context);
                } catch (Throwable e) {
                    lastException = e;
                    try {
                        registerThrowable(retryPolicy, context, e);
                    } catch (Exception ex) {
                        throw new TerminatedRetryException("重试操作终止。注册重试中出现的异常（用于后续分析）失败 {}", ex);
                    }
                    if (canRetry(retryPolicy, context) && !context.isExhaustedOnly()) {
                        logger.info("************ call backOff **************");
                        try {
                            backOffPolicy.backOff(backOffContext);
                        } catch (BackOffInterruptedException ex) {
                            logger.debug("重试中断。已重试 {} 次", context.getRetryCount());
                            throw ex;
                        }
                    }
                }
            }
            exhausted = true;
            return handleRetryExhausted(context);
        } catch (Throwable e) {
            throw RetryTemplate.<E>wrapIfNecessary(e);
        } finally {
            close(retryPolicy, context, lastException == null || exhausted);
        }
    }

    /**
     *
     * @param retryPolicy
     * @param context
     * @return
     */
    protected boolean canRetry(RetryPolicy retryPolicy, RetryContext context) {
        return retryPolicy.canRetry(context);
    }

    /**
     *
     * @param retryPolicy
     * @param context
     * @param succeeded
     */
    protected void close(RetryPolicy retryPolicy, RetryContext context, boolean succeeded) {
        this.retryContext = context;
        //succeed == 重试次数耗尽或无异常
        if (succeeded) {
            context.setRetryStatus(RetryStatusEnum.COMPLETE);
            this.retryContext.setRetryStatus(RetryStatusEnum.COMPLETE);
            retryPolicy.close(context);
        }
    }

    protected void registerThrowable(RetryPolicy retryPolicy, RetryContext context, Throwable e) {
        retryPolicy.registerThrowable(context, e);
    }

    /**
     *
     * @param context
     * @param <T>
     * @return
     * @throws Throwable
     */
    protected <T> T handleRetryExhausted(RetryContext context)
            throws Throwable {
        //TODO 返回默认值
        logger.debug("重试次数已达到上限");
        return null;
//        //TODO 重新抛出异常
//        if (enabledRethrow) {
//            logger.debug("重试次数已达到上限");
//            rethrow(context, "重试次数已达到上限");
//        }
//        //TODO 覆写异常后抛出
//        throw wrapIfNecessary(context.getLastThrowable());
    }

    protected <E extends Throwable> void rethrow(RetryContext context, String message) throws E {
        if (this.throwLastExceptionOnExhausted) {
            @SuppressWarnings("unchecked")
            E rethrow = (E) context.getLastThrowable();
            throw rethrow;
        } else {
            throw new ExhaustedRetryException(message, context.getLastThrowable());
        }
    }

    /**
     *
     */
    private static <E extends Throwable> E wrapIfNecessary(Throwable throwable) throws RetryException {
        if (throwable instanceof Error) {
            throw (Error) throwable;
        } else if (throwable instanceof Exception) {
            @SuppressWarnings("unchecked")
            E rethrow = (E) throwable;
            return rethrow;
        } else {
            throw new RetryException("覆写重试 Throwable 异常，方便最外层捕获", throwable);
        }
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    public BackOffPolicy getBackOffPolicy() {
        return backOffPolicy;
    }

    public void setBackOffPolicy(BackOffPolicy backOffPolicy) {
        this.backOffPolicy = backOffPolicy;
    }

    /**
     * 装配原始retryContext
     * @param originRetryContext 原始retryContext
     */
    public void setContext(RetryContext originRetryContext) {
        this.retryContext = originRetryContext;
    }
}
