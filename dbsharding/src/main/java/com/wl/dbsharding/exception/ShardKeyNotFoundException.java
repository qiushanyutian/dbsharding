package com.wl.dbsharding.exception;

/**
 * Created by wanglei on 2016/10/8.
 */
public class ShardKeyNotFoundException extends Exception {

    public ShardKeyNotFoundException() {
    }

    public ShardKeyNotFoundException(String message) {
        super(message);
    }

    public ShardKeyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
