## 配置中心

![整体设计](./imgs/ConfigCenter.png) 


- Web 管理页面对应 **config-center-ui** 项目

- AdminServer 对应 **config-center-admin** 项目

- 使用 MySQL 做权限校验，同时使用了 Redis。本地启动时需提前安装好以上中间件。

- MySQL 表结构和初始化数据在 **config-center-admin** 项目的 db 目录下

- 配置中心基于 zookeeper 来设计，需提前搭建好 zk 集群。

- client 对应自己的项目，需要引入配置中心 client sdk 与 zk 交互，这个 sdk 即 **spring-boot-starter-config-center** 项目


## Client 端接入方式（SpringBoot）

1. 引入 maven 依赖
    ```xml
        <dependency>
            <groupId>com.configcenter</groupId>
            <artifactId>spring-boot-starter-config-center</artifactId>
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

 