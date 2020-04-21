package com.configcenter.client.core;

import com.configcenter.common.util.CCException;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用 curator 管理 zookeeper
 * <p>
 * Created by mjd on 2020/4/12 15:35
 */
public class CuratorManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CuratorManager.class);

    private String connectString;
    private String nameSpace;
    private Boolean isAdmin;
    private int sessionTimeoutMs = 60 * 1000;
    private int connectionTimeoutMs = 15 * 1000;
    private RetryPolicy retryPolicy;

    private CuratorFramework curatorClient;
    private PathChildrenCache pathChildrenCache;

    public void init() {
        if (connectString == null) {
            throw new CCException("zookeeper 集群地址不能为空");
        }
        if (retryPolicy == null) {
            retryPolicy = new ExponentialBackoffRetry(1000, 3);
        }
        if (nameSpace == null) {
            throw new CCException("缺少 namespace, 需为项目分配唯一的 namespace ");
        }
        // is-admin 用来判断 zk 操作是否加上 namespace 前缀，如果为 true，表示不需要加 namespace，否则需要
        boolean needNamespace = isAdmin == null || !isAdmin;
        curatorClient = CuratorFrameworkFactory.builder()
                .namespace(needNamespace ? nameSpace : null)
                .connectString(connectString)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .build();
        curatorClient.start();

        addListener("/" + nameSpace);
    }

    public void addListener(String path) {
        try {
            pathChildrenCache = new PathChildrenCache(curatorClient, path, true);
            pathChildrenCache.start();
            pathChildrenCache.getListenable().addListener((client, event) -> {
                switch (event.getType()) {
                    case CHILD_ADDED:
                        String currentPathValue = new String(client.getData().storingStatIn(new Stat()).forPath(path));
                        LOGGER.info("CHILD_ADDED,{},parent node data: {}", event.getData().getPath(), currentPathValue);
                        break;
                    case CHILD_UPDATED:
                        LOGGER.info("CHILD_UPDATED,{},vaule:{}", event.getData().getPath(), new String(event.getData().getData()));
                        break;
                    case CHILD_REMOVED:
                        LOGGER.info("CHILD_REMOVED,{}", event.getData().getPath());
                        break;
                    default:
                        break;
                }
            });
        } catch (Exception e) {
            LOGGER.error("配置中心，添加 watch 失败", e);
        }
    }

    public void close() {
        if (curatorClient != null) {
            curatorClient.close();
        }
        if (pathChildrenCache != null) {
            try {
                pathChildrenCache.close();
            } catch (IOException e) {
                LOGGER.error("zookeeper watch listener close exception", e);
            }
        }
    }


    // crud operation

    public boolean add(String key, String value) {
        try {
            curatorClient.create().forPath(fixPath(key), value.getBytes());
        } catch (Exception e) {
            LOGGER.error("!!! 新增配置失败，key:{}, value:{}.", key, value, e);
            return false;
        }
        return true;
    }

    public boolean delete(String key) {
        try {
            curatorClient.delete().forPath(fixPath(key));
        } catch (Exception e) {
            LOGGER.error("!!! 删除配置失败，key:{}.", key, e);
            return false;
        }
        return true;
    }

    public boolean update(String key, String newVal) {
        try {
            curatorClient.setData().forPath(fixPath(key), newVal.getBytes());
        } catch (Exception e) {
            LOGGER.error("!!! 更新配置失败，key:{}, value:{}.", key, newVal, e);
            return false;
        }
        return true;
    }

    public String get(String key) {
        byte[] bytes;
        try {
            bytes = curatorClient.getData().forPath(fixPath(key));
        } catch (Exception e) {
            LOGGER.error("!!! 获取配置失败，key:{}.", key, e);
            return null;
        }
        return new String(bytes);
    }

    public Map<String, String> getAll(String key) {
        Map<String, String> result = new LinkedHashMap<>();
        try {
            GetChildrenBuilder children = curatorClient.getChildren();
            String baseNode = fixPath(key);
            List<String> keyPath = children.forPath(baseNode);
            for (String path : keyPath) {
                try {
                    byte[] bytes = curatorClient.getData().forPath(baseNode + "/" + path);
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

    public CuratorFramework getClient() {
        return curatorClient;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public void setIsAdmin(Boolean watchNamespace) {
        this.isAdmin = watchNamespace;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

}
