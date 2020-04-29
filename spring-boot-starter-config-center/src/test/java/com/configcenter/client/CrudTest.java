package com.configcenter.client;

import com.configcenter.client.core.CuratorManager;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.Map;

/**
 * Created by mjd on 2020/4/12 15:27
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CrudTest {

    private static CuratorManager curatorManager;

    private static String baseNode = "";

    //    @BeforeClass
//    public static void setUp() {
//        curatorManager = new CuratorManager();
//        curatorManager.setConnectString("localhost:2181");
//        curatorManager.setIsAdmin(true);
//        curatorManager.setNameSpace("com.test.app");
//        curatorManager.init();
//    }
    @BeforeClass
    public static void setUp() {
        curatorManager = new CuratorManager();
        curatorManager.setConnectString("localhost:2181");
        curatorManager.setIsAdmin(false);
        curatorManager.setNameSpace("com.test.app");
        curatorManager.init();
    }

    @Test
    public void test1Delete() {
        boolean jdbc = curatorManager.delete(baseNode + "/jdbc");
        boolean add = curatorManager.delete(baseNode + "/username");
        Assert.assertTrue(jdbc && add);
    }

    @Test
    public void test2Add() {
        boolean jdbc = curatorManager.add(baseNode + "/jdbc", "jdbc://192.168.0.1...");
        boolean add = curatorManager.add(baseNode + "/username", "mjd");
        Assert.assertTrue(jdbc && add);
    }

    @Test
    public void test3Update() {
        boolean jdbc = curatorManager.update(baseNode + "/jdbc", "jdbc://192.168.0.1, hhhh");
        boolean update = curatorManager.update(baseNode + "/username", "mjd hhhh");
        Assert.assertTrue(jdbc && update);
    }

    @Test
    public void test4Get() {
        String jdbc = curatorManager.get(baseNode + "/jdbc");
        String username = curatorManager.get(baseNode + "/username");
        Assert.assertEquals(jdbc, "jdbc://192.168.0.1, hhhh");
        Assert.assertEquals(username, "mjd hhhh");
    }

    @Test
    public void test5GetAll() {
        Map s = curatorManager.getAllByAppKey(baseNode);
        System.out.println(s);
    }

    @Test
    public void test6GetAll() {
        Map<String, String> map = curatorManager.getAll();
        System.out.println(map);
    }

    @AfterClass
    public static void release() {
        if (curatorManager != null) {
            curatorManager.close();
        }
    }
}
