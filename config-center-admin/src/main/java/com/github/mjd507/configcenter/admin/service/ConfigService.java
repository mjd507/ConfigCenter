package com.github.mjd507.configcenter.admin.service;

import com.github.mjd507.configcenter.admin.aop.CheckParam;
import com.github.mjd507.configcenter.admin.aop.CheckRead;
import com.github.mjd507.configcenter.admin.aop.CheckWrite;
import com.github.mjd507.configcenter.admin.vo.request.ConfigReq;
import com.github.mjd507.configcenter.client.core.CuratorManager;
import com.github.mjd507.configcenter.client.listener.ConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by mjd on 2020/4/17 11:41
 */
@Service
@Slf4j
public class ConfigService {

    @Autowired
    private CuratorManager configManager;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
        String watchKey = "key1";
        configManager.registerConfigChangeListener(new ConfigChangeListener(watchKey) {
            @Override
            public void onConfigChange(Object newVal) {
                log.info("监听到配置改变，key: {}, val:{}", watchKey, newVal);
            }
        });
    }

    @CheckRead
    public Map<String, String> listAppConfig(@CheckParam Integer appId) {
        String appName = userService.getAppName(appId);
        return configManager.getAllByAppKey(appName);
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

    @CheckWrite
    public boolean deleteConfig(@CheckParam Integer appId, String key) {
        String appName = userService.getAppName(appId);
        String fullPath = appName + "/" + key;
        return configManager.delete(fullPath);
    }
}
