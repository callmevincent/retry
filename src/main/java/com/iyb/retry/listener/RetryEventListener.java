package com.iyb.retry.listener;

import com.alibaba.fastjson.JSONObject;
import com.iyb.retry.RetryContext;
import com.iyb.retry.RetryException;
import com.iyb.retry.bean.RetryResult;
import com.iyb.retry.dao.RetryResultDao;
import com.iyb.retry.service.HalleyFeign;
import com.iyb.retry.support.RetryContextManager;
import com.iyb.retry.support.RetryTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * RetryEventListener
 *
 * @author 2020/3/25 18:08 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
@Component
public class RetryEventListener {
    private static Logger log = LoggerFactory.getLogger(RetryEventListener.class);

    @Autowired
    private HalleyFeign halleyFeign;

    @Autowired
    private RetryResultDao retryResultDao;

    /**
     * 监听重试事件
     *
     * @param originRetryContext retryContext
     */
    @Async
    @EventListener(condition = "#originRetryContext != null ")
    public void retryEventListener(RetryContext originRetryContext) {
        Object result = null;
        RetryTemplate retryTemplate = RetryTemplate.defaultInstance(originRetryContext);
        Throwable th = null;
        try {
            result = retryTemplate.execute(context -> {
                log.info(context.getRetryConfiguration().getArgsString());
                return context.getRetryConfiguration().getPoint().proceed();
            });
        } catch (RetryException e) {
            log.warn("执行重试操作异常 {}", e.getMessage(), e);
            th = e;
        } catch (Throwable throwable) {
            log.error("处理重试事件出错 {}", throwable.getMessage(), throwable);
            th = throwable;
        } finally {
            sendMail(RetryContextManager.getContext(), th);
            saveRetryResult(RetryContextManager.getContext().getRetryResult(result));
            RetryContextManager.clear();
        }
    }

    private String getDetailStackTrace(Throwable e) {
        StackTraceElement[] elements = e.getStackTrace();
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : elements) {
            sb.append(element).append("\t");
        }
        return sb.toString();
    }

    private void sendMail(RetryContext retryContext, Throwable e) {
        try {
            if (retryContext.getLastThrowable() != null) {
                e = retryContext.getLastThrowable();
            } else if (e == null) {
                return;
            }
            JSONObject mailParams = retryContext.buildMailParams(e.getMessage(), getDetailStackTrace(e));
//            log.info("sending email... {} ", mailParams);
            halleyFeign.sendMail(mailParams);
        } catch (Throwable throwable) {
            log.warn("重试失败后，发送邮件通知失败 {}", throwable.getMessage(), throwable);
        }
    }

    private void saveRetryResult(RetryResult retryResult) {
        try {
//            log.info("saving result... {}", JSONObject.toJSONString(retryResult));
            Assert.isTrue(retryResultDao.insert(retryResult) == 1, "插入数据出错") ;
        } catch (Exception e) {
            log.error("保存重试结果出错 {}", e.getMessage(), e);
        }

    }
}
