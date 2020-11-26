package com.wl.dbsharding.common;

import org.apache.ibatis.session.SqlSession;

import javax.annotation.Resource;

public class MyBatisDaoSupport {

    @Resource
    private SqlSession sqlSession;

    public SqlSession getSqlSession() {
        return sqlSession;
    }

}
