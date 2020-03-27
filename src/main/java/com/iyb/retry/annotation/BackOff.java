package com.iyb.retry.annotation;

import java.lang.annotation.*;

/**
 * BackOff
 *
 * @author 2020/3/26 17:45 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BackOff {

    /**
     * 默认一秒重试一次
     * @return 重试间隔(delay == 0时生效)
     */
    long value() default 1000L;

    /**
     *
     * @return > 0 生效
     */
    long delay() default 0;

    /**
     *
     * @return > 0 生效
     */
    long maxDelay() default 0;

    /**
     * 作为重试间隔的倍增系数
     *
     * @return > 0 生效
     */
    double multiplier() default 0;
}
