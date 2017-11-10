package com.pactera.hn.rtds.java.keyValue;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/2.
 */
public class RedisParmKey  implements Serializable{

    private String redisHostName;
    private int redisHostPort;
    private int redisMaxTotalConnects;
    private int redisMaxIdleConnects;
    private int redisMinIdleConnects ;
    private int redisIdleConTimeOUt;
    private boolean redisCheckConnect;
    private boolean redisCheckReturnConn;
    private boolean redisBlockExhaust;
    private long redisMaxWaitTime;

    public RedisParmKey(){

    }

    public String getRedisHostName() {
        return redisHostName;
    }

    public void setRedisHostName(String redisHostName) {
        this.redisHostName = redisHostName;
    }

    public int getRedisHostPort() {
        return redisHostPort;
    }

    public void setRedisHostPort(int redisHostPort) {
        this.redisHostPort = redisHostPort;
    }


    public int getRedisMaxTotalConnects() {
        return redisMaxTotalConnects;
    }

    public void setRedisMaxTotalConnects(int redisMaxTotalConnects) {
        this.redisMaxTotalConnects = redisMaxTotalConnects;
    }

    public int getRedisMaxIdleConnects() {
        return redisMaxIdleConnects;
    }

    public void setRedisMaxIdleConnects(int redisMaxIdleConnects) {
        this.redisMaxIdleConnects = redisMaxIdleConnects;
    }

    public int getRedisMinIdleConnects() {
        return redisMinIdleConnects;
    }

    public void setRedisMinIdleConnects(int redisMinIdleConnects) {
        this.redisMinIdleConnects = redisMinIdleConnects;
    }

    public int getRedisIdleConTimeOUt() {
        return redisIdleConTimeOUt;
    }

    public void setRedisIdleConTimeOUt(int redisIdleConTimeOUt) {
        this.redisIdleConTimeOUt = redisIdleConTimeOUt;
    }

    public boolean isRedisCheckConnect() {
        return redisCheckConnect;
    }

    public void setRedisCheckConnect(boolean redisCheckConnect) {
        this.redisCheckConnect = redisCheckConnect;
    }

    public boolean isRedisCheckReturnConn() {
        return redisCheckReturnConn;
    }

    public void setRedisCheckReturnConn(boolean redisCheckReturnConn) {
        this.redisCheckReturnConn = redisCheckReturnConn;
    }

    public boolean isRedisBlockExhaust() {
        return redisBlockExhaust;
    }

    public void setRedisBlockExhaust(boolean redisBlockExhaust) {
        this.redisBlockExhaust = redisBlockExhaust;
    }

    public long getRedisMaxWaitTime() {
        return redisMaxWaitTime;
    }

    public void setRedisMaxWaitTime(long redisMaxWaitTime) {
        this.redisMaxWaitTime = redisMaxWaitTime;
    }

}
