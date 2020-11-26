package com.wl.dbsharding.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by fanchao on 2016/10/18.
 */
@Aspect
@Component
public class DsProxy extends DataSourceAspect {

    //拦截所有的dao操作
    @Pointcut("@within(com.wl.dbsharding.annotation.DbSharding) " +
            "&& @annotation(com.wl.dbsharding.annotation.DataSource)")
    public void daoMethod() {

    }

}
