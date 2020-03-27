package com.iyb.retry.backoff;

/**
 * 固定睡眠时间的补偿策略
 *
 * @author 2020/3/27 11:34 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public class FixedBackOffPolicy extends AbstractBackOffPolicy implements SleepingBackOffPolicy<FixedBackOffPolicy>{
    private static final Long DEFAULT_PERIOD = 1000L;

    private volatile long backOffPeriod = DEFAULT_PERIOD;

    private Sleeper sleeper = new ThreadWaitSleeper();

    @Override
    public FixedBackOffPolicy retrieveSleeper(Sleeper sleeper) {
        FixedBackOffPolicy policy = new FixedBackOffPolicy();
        policy.setBackOffPeriod(backOffPeriod);
        policy.setSleeper(sleeper);
        return policy;
    }

    /**
     * 定义统一的方法调用
     */
    @Override
    protected void doBackOff() throws BackOffInterruptedException {
        try {
            sleeper.sleep(backOffPeriod);
        } catch (InterruptedException e) {
            throw new BackOffInterruptedException("重试中止于 doBackOff()", e);
        }
    }

    public long getBackOffPeriod() {
        return backOffPeriod;
    }

    public void setBackOffPeriod(long backOffPeriod) {
        //避免人为设置出错
        this.backOffPeriod = backOffPeriod > 0 ? backOffPeriod : 1;
    }

    public void setSleeper(Sleeper sleeper) {
        this.sleeper = sleeper;
    }

    @Override
    public String toString() {
        return "FixedBackOffPolicy{" + "backOffPeriod=" + backOffPeriod + '}';
    }
}
