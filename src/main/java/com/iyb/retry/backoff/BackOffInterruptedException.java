package com.iyb.retry.backoff;

import com.iyb.retry.RetryException;

/**
 * BackOffInterruptedException 用于捕获InterruptedException 时的 错误信息重写
 *
 * @author 2020/3/27 11:55 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public class BackOffInterruptedException extends RetryException {
    public BackOffInterruptedException(String message) {
        super(message);
    }

    public BackOffInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
