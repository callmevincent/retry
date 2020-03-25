package com.iyb.retry;

/**
 * 重试程序终止异常
 *
 * @author 2020/3/25 13:17 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public class TerminatedRetryException extends RetryException {
    public TerminatedRetryException(String message) {
        super(message);
    }

    public TerminatedRetryException(String message, Throwable cause) {
        super(message, cause);
    }
}
