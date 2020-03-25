package com.iyb.retry;

/**
 * ExhaustedRetryException
 *
 * @author 2020/3/25 13:16 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public class ExhaustedRetryException extends RetryException {
    public ExhaustedRetryException(String message) {
        super(message);
    }

    public ExhaustedRetryException(String message, Throwable cause) {
        super(message, cause);
    }
}
