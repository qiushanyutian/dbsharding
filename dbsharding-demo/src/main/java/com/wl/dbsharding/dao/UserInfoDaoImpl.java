package com.wl.dbsharding.dao;

import com.wl.dbsharding.annotation.DataSource;
import com.wl.dbsharding.annotation.DbSharding;
import com.wl.dbsharding.common.MyBatisDaoSupport;
import com.wl.dbsharding.entity.UserInfo;
import org.springframework.stereotype.Repository;

/**
 * @ClassName UserInfoDaoImpl
 * @Description TODO
 * @Author 王雷
 * @Date 2020/11/26 15:26
 */
@DbSharding
@Repository(value = "userInfoDaoImpl")
public class UserInfoDaoImpl extends MyBatisDaoSupport  implements UserInfoMapper{

    @Override
    @DataSource(logicTable = "user",shardKey = "${userInfo.userId}")
    public void addUserInfo(UserInfo userInfo) {
        getSqlSession().insert("UserInfoMapper.addUserInfo",userInfo);
    }
}
