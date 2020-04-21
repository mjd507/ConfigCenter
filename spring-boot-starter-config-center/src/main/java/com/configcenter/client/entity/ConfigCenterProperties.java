package com.configcenter.client.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by mjd on 2020/4/20 08:57
 */
@ConfigurationProperties(prefix = "configcenter")
public class ConfigCenterProperties {

    private String connectString;

    private String namespace;

    private Boolean isAdmin;

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
