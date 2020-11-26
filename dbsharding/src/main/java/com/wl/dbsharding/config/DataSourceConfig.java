package com.wl.dbsharding.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.wl.dbsharding.datasource.DynamicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig implements ApplicationContextAware, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

    private ConfigurableListableBeanFactory beanFactory;

    private static Map<Object, Object> targetDataSources = new HashMap<>();

    @Override
    public void destroy() {
        //释放连接
        for(Object ds : targetDataSources.values()){
            if(ds instanceof DruidDataSource){
                ((DruidDataSource) ds).close();
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        beanFactory = configurableApplicationContext.getBeanFactory();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring")
    public DruidDataSourceConfig druidDataSourceConfig() {
        return new DruidDataSourceConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.pool")
    public PoolConfig poolConfig() {
        return new PoolConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.dbsharding")
    public DbShardRuleConfig dbShardRuleConfig() {
        return new DbShardRuleConfig();
    }

    @Bean
    @Primary
    public DynamicDataSource dataSource(DruidDataSourceConfig druidDataSourceConfig,
                                        PoolConfig poolConfig) throws Exception{

        Map<String, DruidConfig> datasources = druidDataSourceConfig.getDatasources();

        DruidDataSource defaultDataSource = null;
        for (Map.Entry<String, DruidConfig> ds : datasources.entrySet()) {
            DruidConfig druidConfig = ds.getValue();
            DruidDataSource druidDataSource = newDruidDataSource(poolConfig, druidConfig);

            //register single bean
            String beanName = String.format("%sDataSource", ds.getKey());
            beanFactory.registerSingleton(beanName, druidDataSource);

            targetDataSources.put(ds.getKey(), druidDataSource);
            if(defaultDataSource == null && druidConfig.isDefault()){
                defaultDataSource = druidDataSource;
            }
        }

        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
        if(defaultDataSource != null){
            dataSource.setDefaultTargetDataSource(defaultDataSource);
        }
        return dataSource;
    }

    private DruidDataSource newDruidDataSource(PoolConfig poolConfig, DruidConfig druidConfig) throws Exception{
        DruidDataSource datasource = new DruidDataSource();

        datasource.setUrl(druidConfig.getUrl());
        datasource.setUsername(druidConfig.getUsername());
        datasource.setPassword(druidConfig.getPassword());
        datasource.setDriverClassName(druidConfig.getDriverClassName());

        //configuration
        datasource.setInitialSize(poolConfig.getInitialSize());
        datasource.setMinIdle(poolConfig.getMinIdle());
        datasource.setMaxActive(poolConfig.getMaxActive());
        datasource.setMaxWait(poolConfig.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(poolConfig.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(poolConfig.getMinEvictableIdleTimeMillis());
        datasource.setTestOnBorrow(poolConfig.isTestOnBorrow());
        datasource.setTestOnReturn(poolConfig.isTestOnReturn());
        datasource.setTestWhileIdle(poolConfig.isTestWhileIdle());
        datasource.setValidationQuery(poolConfig.getValidationQuery());
        datasource.setPoolPreparedStatements(poolConfig.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(poolConfig
                .getMaxPoolPreparedStatementPerConnectionSize());
        try {
            datasource.setFilters(poolConfig.getFilters());
        } catch (SQLException e) {
            LOGGER.error("druid configuration initialization filter", e);
        }
        datasource.setConnectionProperties(poolConfig.getConnectionProperties());
        datasource.init();
        return datasource;
    }


}