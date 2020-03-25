package com.iyb.retry;

import org.springframework.core.NestedRuntimeException;

/**
 * 重试异常
 *
 * @author 2020/3/25 13:16 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public class RetryException extends NestedRuntimeException {
    private static final long serialVersionUID = 8036371603155209792L;

    public RetryException(String message) {
        super(message);
    }

    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }
}
