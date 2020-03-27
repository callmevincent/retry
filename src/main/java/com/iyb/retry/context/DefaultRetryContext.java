package com.iyb.retry.context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iyb.retry.RetryConfiguration;
import com.iyb.retry.RetryContext;
import com.iyb.retry.bean.RetryResult;
import com.iyb.retry.enums.RetryStatusEnum;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * DefaultRequestContext
 *
 * @author 2020/3/25 18:38 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public class DefaultRetryContext implements RetryContext {

    private RetryStatusEnum status = RetryStatusEnum.FAILED;

    private boolean terminate = false;

    private int retriedTimes;

    private Throwable lastException;

    private RetryContext parent;

//    private List<RetryListener> retryListeners;

    private RetryConfiguration retryConfiguration;

    private String requestId;

    public DefaultRetryContext(RetryContext parent) {
        this.parent = parent;
        if (parent != null) {
            this.retryConfiguration = parent.getRetryConfiguration();
            this.requestId = parent.getContextId();
        }
    }

    public DefaultRetryContext(@NotNull RetryConfiguration retryConfiguration) {
        this.requestId = UUID.randomUUID().toString();
        this.retryConfiguration = retryConfiguration;
    }

    @Override
    public boolean isExhaustedOnly() {
        return terminate;
    }

    @Override
    public void setExhaustedOnly() {
        terminate = true;
    }

    @Override
    public String getContextId() {
        return requestId;
    }

    @Override
    public RetryContext getParent() {
        return parent;
    }

    /**
     * 获取重试次数
     *
     * @return 重试次数
     */
    @Override
    public int getRetryCount() {
        return retriedTimes;
    }

    /**
     * 触发本次重试的上次重试异常
     *
     * @return 获取最近一次重试异常
     */
    @Override
    public Throwable getLastThrowable() {
        return lastException;
    }

    /**
     * 获取重试配置
     */
    @Override
    public RetryConfiguration getRetryConfiguration() {
        return retryConfiguration;
    }

    /**
     * 重试失败更新异常堆栈
     *
     * @param throwable 导致本次重试失败的异常
     */
    public void registerThrowable(Throwable throwable) {
        this.lastException = throwable;
        if (throwable != null) {
            retriedTimes++;
        }
    }

    /**
     * 构建邮件消息
     *
     * @param title 主题补充
     * @param content   正文
     * @return 邮件信息
     */
    @Override
    public JSONObject buildMailParams(String title, String content) {
        return (JSONObject) JSON.toJSON(new MailTemplate(title, content, retryConfiguration.getMailTo()));
    }

    @Override
    public void setRetryStatus(RetryStatusEnum status) {
        this.status = status;
    }

    /**
     * 判断最终重试状态
     *
     * @param result .
     * @return .
     */
    @Override
    public RetryResult getRetryResult(Object result) {
        String resultJson = null;
        String lastExceptionMsg = "";
        if (result != null) {
            this.status = RetryStatusEnum.SUCCEED;
            resultJson = JSONObject.toJSONString(result);
        } else if (this.lastException != null) {
            lastExceptionMsg = this.lastException.getMessage();
        }
        return RetryResult.builder(requestId, this.status.getStatus(), resultJson)
                .clazz(retryConfiguration.getClazz())
                .method(retryConfiguration.getMethod())
                .errorMsg(retryConfiguration.getArgsString() + " 最后一次重试Exception Msg：" + lastExceptionMsg)
                .retryTimes(getRetryCount())
                .gmtCreated(new Date())
                .build();
    }

    @Override
    public String toString() {
        return String.format("[RetryContext: retriedTimes=%d, lastException=%s, exhausted=%b]", retriedTimes, lastException, terminate);
    }

    static class MailTemplate {
        private String fromMail = "passport@mailserver.iyunbao.com";
        private String title = "重试组件异常通知";
        private String userName = "iyunbao";
        private String toMails = "";
        private String bodyHtml = "<p>${content}</p>";

        public MailTemplate(String addToTitle, String content, String toMails) {
            if (addToTitle != null) {
                this.title = this.title + "-" + addToTitle;
            }
            this.bodyHtml = this.bodyHtml.replace("${content}", content);
            this.toMails = toMails;
        }

        public String getFromMail() {
            return fromMail;
        }

        public void setFromMail(String fromMail) {
            this.fromMail = fromMail;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getToMails() {
            return toMails;
        }

        public void setToMails(String toMails) {
            this.toMails = toMails;
        }

        public String getBodyHtml() {
            return bodyHtml;
        }

        public void setBodyHtml(String bodyHtml) {
            this.bodyHtml = bodyHtml;
        }
    }
}
