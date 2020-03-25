package com.iyb.retry.listener;

import com.iyb.retry.context.RetryContextSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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

    /**
     * 监听重试事件
     *
     * @param retryContextSupport RetryContextSupport
     */
    @EventListener(condition = "#retryContextSupport != null ")
    public void retryEventListener(RetryContextSupport retryContextSupport) {
        try {
            Object result = retryContextSupport.doProceed();
        } catch (Throwable e) {
            log.info("{}", e.getMessage(), e);
            //TODO sendMail
        }
        log.info("");
    }
}
