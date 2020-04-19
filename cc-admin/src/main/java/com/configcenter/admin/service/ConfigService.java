package com.configcenter.admin.service;

import com.configcenter.admin.aop.CheckParam;
import com.configcenter.admin.aop.CheckRead;
import com.configcenter.admin.aop.CheckWrite;
import com.configcenter.admin.vo.request.ConfigReq;
import com.configcenter.client.ConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by mjd on 2020/4/17 11:41
 */
@Service
public class ConfigService {

    @Autowired
    private ConfigManager configManager;

    @Autowired
    private UserService userService;

    @CheckRead
    public Map<String, String> listAppConfig(@CheckParam Integer appId) {
        String appName = userService.getAppName(appId);
        return configManager.getAll(appName);
    }

    @CheckWrite
    public boolean writeConfig(ConfigReq configReq, @CheckParam Integer appId) {
        String appName = userService.getAppName(appId);
        boolean add = configReq.getIsAdd();
        String fullPath = appName + "/" + configReq.getKey();
        if (add) {
            return configManager.add(fullPath, configReq.getVal());
        } else {
            return configManager.update(fullPath, configReq.getVal());
        }
    }

}
