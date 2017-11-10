package com.pactera.hn.rtds.java;

/**
 * 预统计的时间类型
 */
public enum  PreStatTimeType {
    DAY(1), HOUR(2), MINUTES(3);

    private final int value;

    PreStatTimeType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
