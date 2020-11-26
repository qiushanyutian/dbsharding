package com.wl.dbsharding.controller;

import com.wl.dbsharding.entity.UserInfo;
import com.wl.dbsharding.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserContoller
 * @Description TODO
 * @Author 王雷
 * @Date 2020/11/26 13:53
 */
@RestController
public class UserContoller {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "/user/add",method = RequestMethod.POST)
    public boolean addUser(@RequestBody UserInfo userInfo){
        userInfoService.addUserInfo(userInfo);
        return true;
    }

    @RequestMapping(value = "/hello")
    public String hello(){
        return "hello";
    }
}
