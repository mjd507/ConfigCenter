package com.configcenter.client.core;

import com.configcenter.common.util.CCException;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 使用 curator 管理 zookeeper
 * <p>
 * Created by mjd on 2020/4/12 15:35
 */
public class CuratorManager {

    private String connectString;
    private String nameSpace;
    private int sessionTimeoutMs = 60 * 1000;
    private int connectionTimeoutMs = 15 * 1000;
    private RetryPolicy retryPolicy;

    private CuratorFramework curatorClient;

    public void init() {
        if (connectString == null) {
            throw new CCException("zookeeper 集群地址不能为空");
        }
        if (retryPolicy == null) {
            retryPolicy = new ExponentialBackoffRetry(1000, 3);
        }
        curatorClient = CuratorFrameworkFactory.builder()
                .namespace(nameSpace)
                .connectString(connectString)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .build();
        curatorClient.start();
    }

    public void close() {
        if (curatorClient != null) {
            curatorClient.close();
        }
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
