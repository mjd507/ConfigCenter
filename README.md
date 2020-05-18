## 配置中心

![整体设计](./imgs/ConfigCenter.png) 

## 接入前提

- 使用 MySQL 做权限校验，同时使用了 Redis。本地启动时需提前安装好以上中间件。

- 配置中心基于 zookeeper 来设计，需提前搭建好 zk 集群。

- 配置中心管理后台前后端分离，分别对应 **config-center-ui** 和 **config-center-admin**

## Client 端接入方式（SpringBoot）

1. 引入 maven 依赖
    ```xml
        <dependency>
           <groupId>com.github.mjd507</groupId>
           <artifactId>config-center-client</artifactId>
           <version>1.0-SNAPSHOT</version>
        </dependency>
    ```
   
2. 配置zk链接信息
    ```yaml
    configcenter:
      namespace: ${spring.application.name} # 每个项目定义唯一的 namespace. 此为监听配置变更的默认路径

      connect-string: localhost:2181 # 多个地址用,分割
    ```

3. 注入 ConfigManager
    ```java
       @Autowired
       private CuratorManager configManager;
    ```
4. 读取配置
     ```java
        configManager.getAll();
        configManager.get(String key);
    ```
5. 监听单个key
    ```java
        String watchKey = "key1";
        configManager.registerConfigChangeListener(new ConfigChangeListener(watchKey) {
            @Override
            public void onConfigChange(Object newVal) {
                log.info("监听到配置改变，key: {}, val:{}", watchKey, newVal);
            }
        });
    ```

 