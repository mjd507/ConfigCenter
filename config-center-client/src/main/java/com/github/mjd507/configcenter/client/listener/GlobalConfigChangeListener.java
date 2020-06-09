package com.github.mjd507.configcenter.client.listener;

/**
 * Created by mjd on 2020/6/9 23:48
 */
public interface GlobalConfigChangeListener {

    void onConfigChange(String key, Object newVal);
}
