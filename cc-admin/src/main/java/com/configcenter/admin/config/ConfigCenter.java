package com.configcenter.admin.config;

import com.configcenter.client.ConfigManager;
import com.configcenter.client.core.ConfigManagerImpl;
import com.configcenter.client.core.CuratorManager;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.zookeeper.data.Stat;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mjd on 2020/4/12 20:32
 */
@Setter
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "zookeeper")
public class ConfigCenter {

    private String connectString;

    private String namespace;

    @Bean
    public ConfigManager configManager() {
        CuratorManager curatorManager = new CuratorManager();
        // curatorManager.setNameSpace(namespace); // 管理后台不设定 namespace，支持查看所有有权限的 namespace 下的配置
        curatorManager.setConnectString(connectString);
        curatorManager.init();
        addListener(curatorManager.getClient(), "/" + namespace);
        return new ConfigManagerImpl(curatorManager);
    }

    public void addListener(CuratorFramework curatorFramework, String path) {
        try {
            PathChildrenCache cache = new PathChildrenCache(curatorFramework, path, true);
            cache.start();
            cache.getListenable().addListener((client, event) -> {
                switch (event.getType()) {
                    case CHILD_ADDED:
                        String currentPathValue = new String(client.getData().storingStatIn(new Stat()).forPath(path));
                        log.info("CHILD_ADDED,{},parent node data: {}", event.getData().getPath(), currentPathValue);
                        break;
                    case CHILD_UPDATED:
                        log.info("CHILD_UPDATED,{},vaule:{}", event.getData().getPath(), new String(event.getData().getData()));
                        break;
                    case CHILD_REMOVED:
                        log.info("CHILD_REMOVED,{}", event.getData().getPath());
                        break;
                    default:
                        break;
                }
            });
        } catch (Exception e) {
            log.error("配置中心，添加 watch 失败", e);
        }
    }
}
