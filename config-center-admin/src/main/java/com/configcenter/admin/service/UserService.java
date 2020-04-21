package com.configcenter.admin.service;

import com.configcenter.admin.entity.UserAppRole;
import com.configcenter.admin.entity.User;
import com.configcenter.admin.mapper.UserMapper;
import com.configcenter.common.util.Md5Utils;
import com.configcenter.common.util.RedisOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by mjd on 2020/4/16 16:51
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisOperation redisOperation;

    public String login(String name, String pwd) {
        User user = userMapper.selectUserByNameAndPwd(name, Md5Utils.digist(pwd));
        if (user == null) {
            return null;
        }
        String accessToken = UUID.randomUUID().toString().replace("-", "");
        redisOperation.set(accessToken, user.getId() + "", 30 * 24 * 60 * 60);
        return accessToken;
    }

    public static void main(String[] args) {
        System.out.println(Md5Utils.digist("123456"));
    }
    public User getUserById(Integer uId) {
        return userMapper.selectUserById(uId);
    }

    public List<UserAppRole> listUserAppsWithRole(int uid) {
        return userMapper.selectUserAppsWithRole(uid);
    }

    public Integer getRoleTypeByUidAndAppId(int uid, int appId) {
        return userMapper.selectRoleTypeByUidAndAppId(uid, appId);
    }

    public String getAppName(int appId) {
        return userMapper.selectAppNameByAppId(appId);
    }
}
