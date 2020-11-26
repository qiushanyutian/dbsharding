package com.wl.dbsharding.config;

import java.util.Map;

/**
 * datasource集合
 */
public class DruidDataSourceConfig {

    private Map<String, DruidConfig> datasources;

    public Map<String, DruidConfig> getDatasources() {
        return datasources;
    }

    public void setDatasources(Map<String, DruidConfig> datasources) {
        this.datasources = datasources;
    }


}
