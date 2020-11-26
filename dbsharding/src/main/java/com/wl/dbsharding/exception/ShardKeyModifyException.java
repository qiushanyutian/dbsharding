package com.wl.dbsharding.exception;

/**
 * Created by wanglei on 2016/10/8.
 */
public class ShardKeyModifyException extends Exception {

    public ShardKeyModifyException() {
    }

    public ShardKeyModifyException(String message) {
        super(message);
    }

    public ShardKeyModifyException(String message, Throwable cause) {
        super(message, cause);
    }
}
