package com.wl.dbsharding.config;

import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 逻辑表分库配置
 */
public class DbShardRuleConfig {

    private Map<String, List<String>> rules;

    public Map<String, List<String>> getRules() {
        return rules;
    }

    public void setRules(Map<String, List<String>> rules) {
        this.rules = rules;
    }

    public String selectDataSource(long shardKey, String logicTable){
        if(shardKey < 0 || StringUtils.isEmpty(logicTable)
                || rules == null || rules.isEmpty()
                || !rules.containsKey(logicTable)){
            return "";
        }

        List<String> rules = this.rules.get(logicTable);
        if(rules.size() == 1){//单表直接返回
            return rules.get(0);
        } else{ //分库取模
            return rules.get((int) (shardKey % rules.size()));
        }
    }
}
