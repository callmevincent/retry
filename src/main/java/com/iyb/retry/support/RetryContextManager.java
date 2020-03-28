package com.iyb.retry.support;

import com.iyb.retry.RetryContext;

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
