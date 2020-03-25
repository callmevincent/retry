package com.iyb.retry;

import com.iyb.retry.annotation.RetryStrategy;

public class RetryConfiguration {
    private RetryStrategy retryStrategy;
    private int maxAttempts;

    public RetryConfiguration(RetryStrategy retryStrategy) {
        this.maxAttempts = retryStrategy.maxAttempts();
    }


    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
}
