package com.configcenter.client;

import com.configcenter.client.core.CuratorManager;

import java.util.Map;

/**
 * Created by mjd on 2020/4/12 13:42
 */
public interface ConfigManager {

    /**
     * 获取 curator 管理，使用 curator 来操作 zk
     */
    CuratorManager getCuratorManager();

    /**
     * 添加配置
     */
    boolean add(String key, String value);

    /**
     * 删除配置
     */
    boolean delete(String key);

    /**
     * 更新配置
     */
    boolean update(String key, String newVal);

    /**
     * 获取配置
     */
    String get(String key);

    /**
     * 获取配置
     */
    Map<String, String> getAll(String key);
}
