package com.wl.dbsharding.util;

public class DatabaseContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
    public static void setDbType(String dbType) {
        contextHolder.set(dbType);    
    }    
    
    public static String getDbType() {    
        return ((String) contextHolder.get());    
    }    
    
    public static void clearDbType() {    
        contextHolder.remove();    
    }    

}
