package com.wl.dbsharding.annotation;


import java.lang.annotation.*;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String logicTable();
    String shardKey() default "";
}

