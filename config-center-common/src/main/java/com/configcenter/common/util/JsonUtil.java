package com.configcenter.common.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Create by majiandong on 2019/11/26 16:22
 */
@Slf4j
public class JsonUtil {
    private JsonUtil() {
    }

    public static String toJsonStr(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T> T toObj(String json, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(json, cls);
        } catch (JsonSyntaxException e) {
            log.error("json string to clazz failed. json string:{}, clazz:{}", json, cls.getSimpleName());
        }
        return t;
    }

    public static <T> List<T> toList(String json, Class<T> cls) {
        List<T> ts = null;
        try {
            Gson gson = new Gson();
            ts = gson.fromJson(json, new TypeToken<List<T>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            log.error("json string to list failed. json string:{}, clazz:{}", json, cls.getSimpleName());
        }
        return ts;
    }

}
