package com.iyb.retry;

import com.alibaba.fastjson.JSONObject;
import com.iyb.retry.annotation.BackOff;
import com.iyb.retry.annotation.RetryStrategy;
import com.iyb.retry.enums.BackOffPolicyEnum;
import com.iyb.retry.enums.RetryPolicyEnum;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Objects;

/**
 * @author iyb-wangyanbing
 */
public class RetryConfiguration {
    private transient ProceedingJoinPoint point;
    private transient RetryStrategy retryStrategy;
    private int maxAttempts;
    private String mailTo;
    private String argsString;

    private RetryPolicyEnum retryPolicyEnum;
    private BackOffPolicyEnum backOffPolicyEnum;

    public RetryConfiguration(ProceedingJoinPoint point, RetryStrategy retryStrategy) {
        Objects.requireNonNull(point, "point 不能为空");
        Objects.requireNonNull(retryStrategy, "retryStrategy 不能为空");
        this.point = point;
        this.retryStrategy = retryStrategy;
        setArgsString();
        resolveRetryStrategy(retryStrategy);
    }

    private void resolveRetryStrategy(RetryStrategy retryStrategy) {
        this.maxAttempts = retryStrategy.maxAttempts();
        this.mailTo = retryStrategy.mailTo();
        this.retryPolicyEnum = retryStrategy.retryPolicy();
        this.backOffPolicyEnum = retryStrategy.backOffPolicy();
    }

    public ProceedingJoinPoint getPoint() {
        return point;
    }

    public void setPoint(ProceedingJoinPoint point) {
        this.point = point;
    }

    public RetryStrategy getRetryStrategy() {
        return retryStrategy;
    }

    public void setRetryStrategy(RetryStrategy retryStrategy) {
        this.retryStrategy = retryStrategy;
    }

    public BackOff getBackOff() {
        return retryStrategy.backoff();
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getClazz() {
        return point.getSignature().getDeclaringType().getSimpleName();
    }

    public String getMethod() {
        return point.getSignature().getName();
    }

    public String getArgsString() {
        return argsString;
    }

    public RetryPolicyEnum getRetryPolicyEnum() {
        return retryPolicyEnum;
    }

    public BackOffPolicyEnum getBackOffPolicyEnum() {
        return backOffPolicyEnum;
    }

    private void setArgsString() {
        Object[] args  = point.getArgs();
        int length = args.length;
        StringBuilder sb = new StringBuilder();
        if (point.getArgs() != null) {
            for (Object arg : args) {
                try {
                    sb.append("参数：").append(JSONObject.toJSONString(arg)).append(" ");
                } catch (Exception e) {
                    this.argsString = "logPointCutArgs error " + sb.toString();
                }
            }
        }
        this.argsString = sb.toString();
    }
}
