package com.wl.dbsharding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.wl.dbsharding"})
@MapperScan("com.wl.dbsharding.dao")
public class App 
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }
}
