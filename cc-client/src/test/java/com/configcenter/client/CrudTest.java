package com.configcenter.client;

import com.configcenter.client.core.ConfigManagerImpl;
import com.configcenter.client.core.CuratorManager;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.Map;

/**
 * Created by mjd on 2020/4/12 15:27
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CrudTest {

    private static ConfigManager configManager;

    private static String baseNode = "com.test.app";

    @BeforeClass
    public static void setUp() {
        CuratorManager curatorManager = new CuratorManager();
        curatorManager.setConnectString("localhost:2181");
        curatorManager.init();
        configManager = new ConfigManagerImpl(curatorManager);
    }

    @Test
    public void test1Delete() {
        boolean jdbc = configManager.delete(baseNode + "/jdbc");
        boolean add = configManager.delete(baseNode + "/username");
        Assert.assertTrue(jdbc && add);
    }

    @Test
    public void test2Add() {
        boolean jdbc = configManager.add(baseNode + "/jdbc", "jdbc://192.168.0.1...");
        boolean add = configManager.add(baseNode + "/username", "mjd");
        Assert.assertTrue(jdbc && add);
    }

    @Test
    public void test3Update() {
        boolean jdbc = configManager.update(baseNode + "/jdbc", "jdbc://192.168.0.1, hhhh");
        boolean update = configManager.update(baseNode + "/username", "mjd hhhh");
        Assert.assertTrue(jdbc && update);
    }

    @Test
    public void test4Get() {
        String jdbc = configManager.get(baseNode + "/jdbc");
        String username = configManager.get(baseNode + "/username");
        Assert.assertEquals(jdbc, "jdbc://192.168.0.1, hhhh");
        Assert.assertEquals(username, "mjd hhhh");
    }

    @Test
    public void test5GetAll() {
        Map s = configManager.getAll(baseNode);
        System.out.println(s);
    }

    @AfterClass
    public static void release() {
        if (configManager != null) {
            configManager.getCuratorManager().close();
        }
    }
}
