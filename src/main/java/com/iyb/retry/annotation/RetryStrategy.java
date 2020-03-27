package com.iyb.retry.annotation;

import com.iyb.retry.enums.BackOffPolicyEnum;
import com.iyb.retry.enums.RetryPolicyEnum;

import java.lang.annotation.*;

/**
 * RetryStrategy
 *
 * @author 2020/3/25 12:23 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RetryStrategy {

    /**
     * @return Throwable
     */
    Class<? extends Throwable> value() default RuntimeException.class;

    /**
     * 最大重试次数
     *
     * @return maxAttempts
     */
    int maxAttempts() default 3;

    BackOff backoff() default @BackOff();

    /**
     * @see com.iyb.retry.RetryListener
     * @return .
     */
    String[] listener() default {};

    String mailTo() default "wangyanbin@iyunbao.com";

    RetryPolicyEnum retryPolicy() default RetryPolicyEnum.MAX_ATTEMPTS;

    BackOffPolicyEnum backOffPolicy() default BackOffPolicyEnum.FIXED;
}
