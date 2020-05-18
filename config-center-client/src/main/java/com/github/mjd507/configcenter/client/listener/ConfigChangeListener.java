package com.github.mjd507.configcenter.client.listener;

/**
 * Created by mjd on 2020/4/23 22:29
 */
public abstract class ConfigChangeListener {

    private final String key;

    public abstract void onConfigChange(Object newVal);

    public ConfigChangeListener(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
