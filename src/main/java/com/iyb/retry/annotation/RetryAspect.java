package com.iyb.retry.annotation;

import com.alibaba.fastjson.JSONObject;
import com.iyb.retry.RetryConfiguration;
import com.iyb.retry.RetryResult;
import com.iyb.retry.context.RetryContextSupport;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * demo演示如下
 * 出错接管
 * 提取重试策略配置
 * 重试
 * 记录重试错误
 * 重试次数
 * 经重试仍出错
 * 熔断降级或返回默认
 *
 * @author 2020/3/25 12:28 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
@Aspect
@Component
public class RetryAspect {
    private static Logger log = LoggerFactory.getLogger(RetryAspect.class);

    @Autowired
    private ApplicationEventPublisher publisher;

    @Pointcut("execution(public * com.iyb.retry.service.RequestService.run(..))")
    public void pointCutOn() {
    }

    @Around("@annotation(retryStrategy)")
    public Object doAround(ProceedingJoinPoint point, RetryStrategy retryStrategy) {
        Object result;
        try {
            result = point.proceed();
        } catch (Throwable th) {
            log.error("request error {}", th.getMessage(), th);
            String requestId = UUID.randomUUID().toString();
            publisher.publishEvent(new RetryContextSupport(requestId, point, new RetryConfiguration(retryStrategy)));
            return defaultResult(requestId);
        }
        return result;
    }

    //TODO
    private JSONObject defaultResult(String requestId) {
        JSONObject result = new JSONObject();
        result.put("code", 111111);
        result.put("errorMsg", "");
        result.put("data", requestId);
        return result;
    }

//    @AfterThrowing TODO 可以考虑

    static class RetryThread implements Runnable {
        private String requestId;
        private ApplicationEventPublisher publisher;
        private ProceedingJoinPoint point;
        private RetryStrategy anno;

        public RetryThread(String requestId, ApplicationEventPublisher publisher, ProceedingJoinPoint point, RetryStrategy anno) {
            this.requestId = requestId;
            this.publisher = publisher;
            this.point = point;
            this.anno = anno;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            long startRetry = System.currentTimeMillis();
            String clazz = point.getSignature().getDeclaringType().getSimpleName();
            String method = point.getSignature().getName();
            log.info("请求 {}.{} 出错，触发重试~", clazz, method);

            Object result = null;
            RetryResult retryResult = null;
            int retryTimes = 0;
            String errorMsg = "";
            boolean timeout = false;
            String retryStatus = "0";
            for (int i = 0; i < anno.maxAttempts(); i++) {
                try {
                    retryTimes = i;
                    result = point.proceed();
                } catch (Throwable e) {
                    log.warn("{}.{} 第 {} 次重试出错 {}", clazz, method, retryTimes, e.getMessage(), e);
                }
                if (result != null) {
                    log.info("{}.{} {} 次重试成功", clazz, method, retryTimes);
                    break;
                } else if (System.currentTimeMillis() > startRetry + anno.timeoutMillis()) {
                    log.info("{}.{} 重试 {} 次后，达到超时阈值 {} ms", clazz, method, retryTimes, anno.timeoutMillis());
                    timeout = true;
                    break;
                }
            }
            if (result != null) {
                retryStatus = "1";
            } else if (timeout) {
                //重试超时
                retryStatus = "2";
            } else if (retryTimes == anno.maxAttempts() - 1) {
                //重试无果
                retryStatus = "3";
            }
            retryResult = RetryResult.builder(requestId, retryStatus, JSONObject.toJSONString(result))
                    .clazz(clazz)
                    .method(method)
                    .gmtCreated(new Date(startRetry))
                    .retryTimes(retryTimes)
                    .errorMsg(errorMsg)
                    .build();
            publisher.publishEvent(retryResult);
        }
    }
}
