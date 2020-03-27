package com.iyb.retry.enums;

/**
 * TODO
 *
 * @author 2020/3/27 16:01 created by iyb-wangyanbing
 * @version 1.0.0
 * @modifier:
 */
public enum RetryPolicyEnum {
    /**
     * 自测时使用
     */
    SIMPLE,

    /**
     * 仅基于次数的重试
     */
    MAX_ATTEMPTS;

}
