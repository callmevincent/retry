package com.iyb.retry.policy;

import com.iyb.retry.RetryContext;
import com.iyb.retry.RetryPolicy;

public class MaxAttemptsRetryPolicy implements RetryPolicy {
    @Override
    public boolean canRetry(RetryContext context) {
        return context.getRetryCount() < context.getRetryConfiguration().getMaxAttempts();
    }

    @Override
    public RetryContext open(RetryContext parent) {
        return null;
    }

    @Override
    public void close(RetryContext context) {

    }

    @Override
    public void registerThrowable(RetryContext context, Throwable throwable) {

    }
}
