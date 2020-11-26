package com.wl.dbsharding.aop;

import com.wl.dbsharding.annotation.DataSource;
import com.wl.dbsharding.config.DbShardRuleConfig;
import com.wl.dbsharding.exception.DsNotFoundException;
import com.wl.dbsharding.util.DatabaseContextHolder;
import com.wl.dbsharding.util.MethodParamNamesScaner;
import org.apache.commons.lang3.math.NumberUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.List;

public class DataSourceAspect implements Ordered {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataSourceAspect.class);

    @Autowired
    private DbShardRuleConfig dbShardRuleConfig;

    @Override
    public int getOrder() {
        return 1;
    }


    //拦截所有的service操作
    @Pointcut("@within(com.wl.dbsharding.annotation.DbSharding) " +
            "&& @annotation(com.wl.dbsharding.annotation.DataSource)")
    public void serviceMethod() {
    }

    /**
     * mybatis生成的daoMapper类是com.sun.Proxy$代理类，注解被擦除
     * 所以dao的aop切面在应用程序自己编写Pointcut->daoMethod()
     */
    @Around("daoMethod()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        DataSource dataSource = method.getAnnotation(DataSource.class);
        setDbType(dataSource, pjp.getArgs(), method);
        Object result = pjp.proceed();
        DatabaseContextHolder.clearDbType();
        return result;
    }

    protected void setDbType(DataSource dataSource, Object[] args,
                             Method method) throws Exception {
        if (null != dataSource) {
            List<String> dsList = dbShardRuleConfig.getRules().get(dataSource.logicTable());
            String shardKey = "0";
            if (dsList != null && dsList.size() > 1) {//分库分表
                shardKey = MethodParamNamesScaner.getKeyNameFromParam(dataSource.shardKey(), args, method);
            }

            if(StringUtils.isEmpty(shardKey) || !NumberUtils.isNumber(shardKey)
                    || NumberUtils.createLong(shardKey) < 0){
                throw new IllegalArgumentException("shardKey must be a non-negative number");
            }

            String dsName = dbShardRuleConfig.selectDataSource(
                    Long.valueOf(shardKey), dataSource.logicTable());
            System.out.println("1111111111111111111="+dsName);
            if(StringUtils.isEmpty(dsName)){
                LOGGER.error("method:{}, not found dsName", method.getName());
                throw new DsNotFoundException("ds not found, must provide shard key");
            }
            DatabaseContextHolder.setDbType(dsName);
        }
    }
}