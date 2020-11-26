package com.wl.dbsharding.exception;

/**
 * Created by wanglei on 2016/10/8.
 */
public class DsNotFoundException extends Exception {

    public DsNotFoundException() {
    }

    public DsNotFoundException(String message) {
        super(message);
    }

    public DsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
