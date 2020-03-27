package com.iyb.retry.support;

import com.iyb.retry.RetryConfiguration;
import com.iyb.retry.RetryContext;
import com.iyb.retry.RetryPolicy;
import com.iyb.retry.backoff.BackOffPolicy;
import com.iyb.retry.backoff.FixedBackOffPolicy;
import com.iyb.retry.enums.BackOffPolicyEnum;
import com.iyb.retry.enums.RetryPolicyEnum;
import com.iyb.retry.policy.MaxAttemptsRetryPolicy;
import com.iyb.retry.policy.SimpleRetryPolicy;
import org.springframework.util.Assert;

/**
 * 使用独立的Builder构建器
 *
 * @author 2020/3/27 14:30 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public class RetryTemplateBuilder {
    private RetryContext originRetryContext;
    private RetryPolicy baseRetryPolicy;
    private BackOffPolicy backOffPolicy;

    public RetryTemplateBuilder originRetryContext(RetryContext originRetryContext) {
        Assert.isNull(this.originRetryContext, "originRetryContext 已设置");
        this.originRetryContext = originRetryContext;
        return this;
    }

    public RetryTemplateBuilder maxAttemptsRetry(int maxAttempts) {
        Assert.isTrue(maxAttempts > 0, "maxAttempts > 0");
        Assert.isNull(this.baseRetryPolicy, "已设置了其他的重试策略");
        this.baseRetryPolicy = new MaxAttemptsRetryPolicy(maxAttempts);
        return this;
    }

    public RetryTemplateBuilder complexBackOff(long initialInterval, double multiplier, long maxInterval) {
        //TODO 按特定规则
        return null;
    }

    public RetryTemplateBuilder fixedBackOff(long interval) {
        Assert.isNull(this.backOffPolicy, "已设置了其他的重试补偿策略");
        Assert.isTrue(interval >= 1, "定长补偿策略的重试间隔  >= 1");
        FixedBackOffPolicy policy = new FixedBackOffPolicy();
        policy.setBackOffPeriod(interval);
        this.backOffPolicy = policy;
        return this;
    }

    private void determineRetryPolicy() {
        RetryConfiguration config = originRetryContext.getRetryConfiguration();
        if (RetryPolicyEnum.MAX_ATTEMPTS == config.getRetryPolicyEnum()) {
            this.maxAttemptsRetry(config.getMaxAttempts());
        } else {
            //TODO
        }
    }

    private void determineBackOffPolicy() {
        RetryConfiguration config = originRetryContext.getRetryConfiguration();
        if (BackOffPolicyEnum.FIXED == config.getBackOffPolicyEnum()) {
            this.fixedBackOff(config.getRetryStrategy().backoff().value());
        } else {
            //TODO
        }
    }

    public RetryTemplate build() {
        RetryTemplate retryTemplate = new RetryTemplate();
        determineRetryPolicy();
        determineBackOffPolicy();

        if (this.baseRetryPolicy == null) {
            this.baseRetryPolicy = new SimpleRetryPolicy();
        }
        retryTemplate.setRetryPolicy(this.baseRetryPolicy);

        if (this.backOffPolicy == null) {
            this.backOffPolicy = new FixedBackOffPolicy();
        }
        retryTemplate.setBackOffPolicy(this.backOffPolicy);

        retryTemplate.setContext(this.originRetryContext);
        return retryTemplate;
    }

}
