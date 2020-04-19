package com.configcenter.client.core;

import com.configcenter.client.ConfigManager;
import com.configcenter.common.util.CCException;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置管理
 * <p>
 * Created by mjd on 2020/4/12 15:05
 */
public class ConfigManagerImpl implements ConfigManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManagerImpl.class);

    private CuratorManager curatorManager;

    public ConfigManagerImpl(CuratorManager curatorManager) {
        this.curatorManager = curatorManager;
    }

    public CuratorManager getCuratorManager() {
        return this.curatorManager;
    }

    // crud operation

    public boolean add(String key, String value) {
        try {
            curatorManager.getClient().create().forPath(fixPath(key), value.getBytes());
        } catch (Exception e) {
            LOGGER.error("!!! 新增配置失败，key:{}, value:{}.", key, value, e);
            return false;
        }
        return true;
    }

    public boolean delete(String key) {
        try {
            curatorManager.getClient().delete().forPath(fixPath(key));
        } catch (Exception e) {
            LOGGER.error("!!! 删除配置失败，key:{}.", key, e);
            return false;
        }
        return true;
    }

    public boolean update(String key, String newVal) {
        try {
            curatorManager.getClient().setData().forPath(fixPath(key), newVal.getBytes());
        } catch (Exception e) {
            LOGGER.error("!!! 更新配置失败，key:{}, value:{}.", key, newVal, e);
            return false;
        }
        return true;
    }

    public String get(String key) {
        byte[] bytes;
        try {
            bytes = curatorManager.getClient().getData().forPath(fixPath(key));
        } catch (Exception e) {
            LOGGER.error("!!! 获取配置失败，key:{}.", key, e);
            return null;
        }
        return new String(bytes);
    }

    public Map<String, String> getAll(String key) {
        Map<String, String> result = new LinkedHashMap<>();
        try {
            GetChildrenBuilder children = curatorManager.getClient().getChildren();
            String baseNode = fixPath(key);
            List<String> keyPath = children.forPath(baseNode);
            for (String path : keyPath) {
                try {
                    byte[] bytes = curatorManager.getClient().getData().forPath(baseNode + "/" + path);
                    result.put(path, new String(bytes));
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            LOGGER.error("!!! 获取配置失败，key:{}.", "/", e);
            return null;
        }
        return result;
    }

    public void watch(String key) {
    }

    /**
     * 使 key 以 /xxx 的形式体现
     */
    private String fixPath(String key) {
        if (key == null) throw new CCException("节点 key 不可为 null");
        if (!key.startsWith("/")) key = "/" + key;
        if (key.endsWith("/") && key.length() > 1) key = key.substring(0, key.length() - 1);
        return key;
    }
}
