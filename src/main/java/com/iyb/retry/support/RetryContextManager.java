package com.iyb.retry.support;

import com.iyb.retry.RetryContext;

/**
 * RetryContextManager
 *
 * @author 2020/3/30 10:43 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public final class RetryContextManager {
	private static final ThreadLocal<RetryContext> CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

	private RetryContextManager() {
	}

	public static RetryContext getContext() {
		return CONTEXT_THREAD_LOCAL.get();
	}

	public static void register(RetryContext context) {
		CONTEXT_THREAD_LOCAL.set(context);
	}

	public static void clear() {
		CONTEXT_THREAD_LOCAL.remove();
	}
}
