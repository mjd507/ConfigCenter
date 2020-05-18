package com.github.mjd507.configcenter.client.config;

/**
 * Created by mjd on 2020/4/20 08:59
 */

import com.github.mjd507.configcenter.client.core.CuratorManager;
import com.github.mjd507.configcenter.client.entity.ConfigCenterProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ConfigCenterProperties.class)
@EnableConfigurationProperties(ConfigCenterProperties.class)
public class ConfigCenterAutoConfiguration {

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(name = "curatorManager")
    public CuratorManager curatorManager(ConfigCenterProperties configCenterProperties) {
        CuratorManager curatorManager = new CuratorManager();
        String namespace = configCenterProperties.getNamespace();
        String connectString = configCenterProperties.getConnectString();
        Boolean isAdmin = configCenterProperties.getIsAdmin();
        curatorManager.setNameSpace(namespace);
        curatorManager.setIsAdmin(isAdmin);
        curatorManager.setConnectString(connectString);
        curatorManager.init();
        return curatorManager;
    }

}
