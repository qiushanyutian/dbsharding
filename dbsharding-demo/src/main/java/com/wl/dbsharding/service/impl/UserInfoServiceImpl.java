package com.wl.dbsharding.service.impl;

import com.wl.dbsharding.annotation.DataSource;
import com.wl.dbsharding.annotation.DbSharding;
import com.wl.dbsharding.dao.UserInfoDaoImpl;
import com.wl.dbsharding.entity.UserInfo;
import com.wl.dbsharding.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserInfoServiceImpl
 * @Description TODO
 * @Author 王雷
 * @Date 2020/11/26 14:15
 */
@Service
@DbSharding
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoDaoImpl userInfoDaoImpl;

    @Override
    @DataSource(logicTable = "user",shardKey = "${userInfo.userId}")
    public boolean addUserInfo(UserInfo userInfo) {
        userInfoDaoImpl.addUserInfo(userInfo);
        return true;
    }
}
