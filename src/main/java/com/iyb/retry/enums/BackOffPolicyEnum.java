package com.iyb.retry.enums;

/**
 * TODO
 *
 * @author 2020/3/27 16:05 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public enum BackOffPolicyEnum {
    /**
     * 无时间补偿
     */
    NO,

    /**
     * 定长补偿
     */
    FIXED;
}
