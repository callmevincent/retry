package com.iyb.retry.annotation;

import java.lang.annotation.*;

/**
 * TODO
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
     * 捕获此类异常
     * @return Throwable
     */
    Class<? extends Throwable> value() default RuntimeException.class;

    /**
     * 最大重试次数
     * @return maxAttempts
     */
    int maxAttempts() default 3;

    /**
     *
     * @return
     */
    int timeoutMillis() default 1000;


    int multiply() default 1;


    String mailTo() default "caojianxiang@iyunbao.com";
}
